/**
 * @fileoverview LineTypeSeriesBase is base class for line type series.
 * @author NHN Ent.
 *         FE Development Lab <dl_javascript@nhnent.com>
 */

'use strict';

var seriesTemplate = require('./seriesTemplate');
var chartConst = require('../const');
var predicate = require('../helpers/predicate');
var renderUtil = require('../helpers/renderUtil');

var concat = Array.prototype.concat;

/**
 * @classdesc LineTypeSeriesBase is base class for line type series.
 * @class LineTypeSeriesBase
 * @mixin
 */
var LineTypeSeriesBase = tui.util.defineClass(/** @lends LineTypeSeriesBase.prototype */ {
    /**
     * Make basic positions for rendering line graph.
     * @param {number} [seriesWidth] - width of series area
     * @returns {Array.<Array.<object>>}
     * @private
     */
    _makeBasicPositions: function(seriesWidth) {
        var dimension = this.boundsMaker.getDimension('series'),
            seriesDataModel = this._getSeriesDataModel(),
            width = seriesWidth || dimension.width || 0,
            height = dimension.height,
            len = seriesDataModel.getGroupCount(),
            start = chartConst.SERIES_EXPAND_SIZE,
            step;

        if (this.data.aligned) {
            step = width / (len - 1);
        } else {
            step = width / len;
            start += (step / 2);
        }

        return seriesDataModel.map(function(seriesGroup) {
            return seriesGroup.map(function(seriesItem, index) {
                var position = {
                    left: start + (step * index),
                    top: height - (seriesItem.ratio * height) + chartConst.SERIES_EXPAND_SIZE
                };

                if (tui.util.isExisty(seriesItem.startRatio)) {
                    position.startTop = height - (seriesItem.startRatio * height) + chartConst.SERIES_EXPAND_SIZE;
                }

                return position;
            });
        }, true);
    },

    /**
     * Calculate label position top.
     * @param {{top: number, startTop: number}} basePosition - base position
     * @param {number} value - value of seriesItem
     * @param {number} labelHeight - label height
     * @param {boolean} isStart - whether start value of seriesItem or not
     * @returns {number} position top
     * @private
     */
    _calculateLabelPositionTop: function(basePosition, value, labelHeight, isStart) {
        var baseTop = basePosition.top,
            top;

        if (predicate.isValidStackOption(this.options.stackType)) {
            top = (basePosition.startTop + baseTop - labelHeight) / 2 + 1;
        } else if ((value >= 0 && !isStart) || (value < 0 && isStart)) {
            top = baseTop - labelHeight - chartConst.SERIES_LABEL_PADDING;
        } else {
            top = baseTop + chartConst.SERIES_LABEL_PADDING;
        }

        return top;
    },

    /**
     * Make label position for rendering label of series area.
     * @param {{left: number, top: number, startTop: ?number}} basePosition - base position for calculating
     * @param {number} labelHeight - label height
     * @param {(string | number)} label - label of seriesItem
     * @param {number} value - value of seriesItem
     * @param {boolean} isStart - whether start label position or not
     * @returns {{left: number, top: number}}
     * @private
     */
    _makeLabelPosition: function(basePosition, labelHeight, label, value, isStart) {
        var labelWidth = renderUtil.getRenderedLabelWidth(label, this.theme.label);
        var dimension = this.boundsMaker.getDimension('extendedSeries');

        return {
            left: (basePosition.left - (labelWidth / 2)) / dimension.width * 100,
            top: this._calculateLabelPositionTop(basePosition, value, labelHeight, isStart) / dimension.height * 100
        };
    },

    /**
     * Make html for series label for line type chart.
     * @param {number} groupIndex - index of seriesDataModel.groups
     * @param {number} index - index of seriesGroup.items
     * @param {SeriesItem} seriesItem - series item
     * @param {number} labelHeight - label height
     * @param {boolean} isStart - whether start label position or not
     * @returns {string}
     * @private
     */
    _makeSeriesLabelHtmlForLineType: function(groupIndex, index, seriesItem, labelHeight, isStart) {
        var basePosition = tui.util.extend({}, this.seriesData.groupPositions[groupIndex][index]),
            label, position;

        if (isStart) {
            label = seriesItem.startLabel;
            basePosition.top = basePosition.startTop;
        } else {
            label = seriesItem.endLabel;
        }

        position = this._makeLabelPosition(basePosition, labelHeight, label, seriesItem.value, isStart);

        return this._makeSeriesLabelHtml(position, label, groupIndex, seriesTemplate.tplCssTextForLineType, isStart);
    },

    /**
     * Render series label.
     * @param {HTMLElement} elSeriesLabelArea series label area element
     * @private
     */
    _renderSeriesLabel: function(elSeriesLabelArea) {
        var self = this,
            seriesDataModel = this._getSeriesDataModel(),
            firstLabel = seriesDataModel.getFirstItemLabel(),
            labelHeight = renderUtil.getRenderedLabelHeight(firstLabel, this.theme.label),
            htmls;

        htmls = seriesDataModel.map(function(seriesGroup, groupIndex) {
            return seriesGroup.map(function(seriesItem, index) {
                var labelHtml = self._makeSeriesLabelHtmlForLineType(groupIndex, index, seriesItem, labelHeight);

                if (seriesItem.isRange) {
                    labelHtml += self._makeSeriesLabelHtmlForLineType(groupIndex, index, seriesItem, labelHeight, true);
                }

                return labelHtml;
            }).join('');
        }, true);

        elSeriesLabelArea.innerHTML = htmls.join('');
    },

    /**
     * Whether changed or not.
     * @param {number} groupIndex group index
     * @param {number} index index
     * @returns {boolean} whether changed or not
     * @private
     */
    _isChanged: function(groupIndex, index) {
        var prevIndexes = this.prevIndexes;

        this.prevIndexes = {
            groupIndex: groupIndex,
            index: index
        };

        return !prevIndexes || (prevIndexes.groupIndex !== groupIndex) || (prevIndexes.index !== index);
    },

    /**
     * To call showGroupTooltipLine function of graphRenderer.
     * @param {{
     *      dimension: {width: number, height: number},
     *      position: {left: number, top: number}
     * }} bound bound
     */
    onShowGroupTooltipLine: function(bound) {
        if (!this.graphRenderer.showGroupTooltipLine) {
            return;
        }

        this.graphRenderer.showGroupTooltipLine(bound);
    },

    /**
     * To call hideGroupTooltipLine function of graphRenderer.
     */
    onHideGroupTooltipLine: function() {
        if (!this.graphRenderer.hideGroupTooltipLine) {
            return;
        }
        this.graphRenderer.hideGroupTooltipLine();
    },

    /**
     * Zoom by mouse drag.
     * @param {object} data - data
     * @returns {{container: HTMLElement, paper: object}}
     */
    zoom: function(data) {
        var paper;

        this._cancelMovingAnimation();
        this._clearContainer(data.paper);
        paper = this._renderSeriesArea(this.seriesContainer, data, tui.util.bind(this._renderGraph, this));
        this._showGraphWithoutAnimation();

        if (!tui.util.isNull(this.selectedLegendIndex)) {
            this.graphRenderer.selectLegend(this.selectedLegendIndex);
        }

        return {
            container: this.seriesContainer,
            paper: paper
        };
    },

    /**
     * Whether changed limit(min, max) or not.
     * @returns {boolean}
     * @private
     */
    _isChangedLimit: function() {
        var beforeLimit = this.data.limit;
        var afterLimit = this.boundsMaker.getAxesData().yAxis.limit;

        return beforeLimit.min !== afterLimit.min || beforeLimit.max !== afterLimit.max;
    },

    /**
     * Animate for motion of series area.
     * @param {function} callback - callback function
     * @private
     */
    _animate: function(callback) {
        var self = this;
        var changedLimit = this._isChangedLimit();

        this.movingAnimation = renderUtil.startAnimation(300, function(ratio) {
            if (changedLimit && self.seriesLabelContainer) {
                self.seriesLabelContainer.innerHTML = '';
            }
            callback(ratio);
        }, function() {
            self.movingAnimation = null;
        });
    },

    /**
     * Pick first label elements.
     * @returns {Array.<HTMLElement>}
     * @private
     */
    _pickFirstLabelElements: function() {
        var itemCount = this.dataProcessor.getCategoryCount() - 1;
        var seriesLabelContainer = this.seriesLabelContainer;
        var labelElements = seriesLabelContainer.childNodes;
        var filteredElements = [];
        var firstLabelElements;

        tui.util.forEachArray(labelElements, function(element) {
            if (!element.getAttribute('data-range')) {
                filteredElements.push(element);
            }
        });
        filteredElements = tui.util.filter(filteredElements, function(element, index) {
            return ((parseInt(index, 10) + 1) % itemCount) === 1;
        });
        firstLabelElements = tui.util.map(filteredElements, function(element) {
            var nextElement = element.nextSibling;
            var elements = [element];

            if (nextElement && nextElement.getAttribute('data-range')) {
                elements.push(nextElement);
            }

            return elements;
        });

        return concat.apply([], firstLabelElements);
    },

    /**
     * Hide first labels.
     * @private
     */
    _hideFirstLabels: function() {
        var seriesLabelContainer = this.seriesLabelContainer;
        var firsLabelElements;

        if (!seriesLabelContainer) {
            return;
        }

        firsLabelElements = this._pickFirstLabelElements();
        tui.util.forEachArray(firsLabelElements, function(element) {
            seriesLabelContainer.removeChild(element);
        });
    },

    /**
     * Animate for moving of graph container.
     * @param {number} interval - interval for moving
     * @private
     */
    _animateForMoving: function(interval) {
        var graphRenderer = this.graphRenderer;
        var childrenForMoving = this.seriesContainer.childNodes;
        var areaWidth = this.boundsMaker.getDimension('extendedSeries').width;
        var beforeLeft = 0;

        this._hideFirstLabels();

        if (childrenForMoving.length) {
            beforeLeft = parseInt(childrenForMoving[0].style.left, 10) || 0;
        }

        this._animate(function(ratio) {
            var left = interval * ratio;

            tui.util.forEachArray(childrenForMoving, function(child) {
                child.style.left = (beforeLeft - left) + 'px';
            });

            graphRenderer.setSize(areaWidth + left);
        });
    },

    /**
     * Animate for resizing of label container.
     * @param {number} interval - interval for stacking
     * @private
     */
    _animateForResizing: function(interval) {
        var seriesLabelContainer = this.seriesLabelContainer;
        var areaWidth;

        if (!seriesLabelContainer) {
            return;
        }

        areaWidth = this.boundsMaker.getDimension('extendedSeries').width;

        this._animate(function(ratio) {
            var left = interval * ratio;

            seriesLabelContainer.style.width = (areaWidth - left) + 'px';
        });
    },

    /**
     * Make top of zero point for adding data.
     * @returns {number}
     * @private
     * @override
     */
    _makeZeroTopForAddingData: function() {
        var seriesHeight = this.boundsMaker.getDimension('series').height;
        var limit = this.boundsMaker.getAxesData().yAxis.limit;

        return this._getLimitDistanceFromZeroPoint(seriesHeight, limit).toMax + chartConst.SERIES_EXPAND_SIZE;
    },

    /**
     * Animate for adding data.
     * @param {{tickSize: number}} params - parameters for adding data.
     */
    animateForAddingData: function(params) {
        var seriesData = this._makeSeriesData();
        var dimension = this.boundsMaker.getDimension('extendedSeries');
        var seriesWidth = this.boundsMaker.getDimension('series').width;
        var paramsForRendering = this._makeParamsForGraphRendering(dimension, seriesData);
        var tickSize = params.tickSize;
        var shiftingOption = this.options.shifting;
        var groupPositions, zeroTop;

        if (shiftingOption) {
            seriesWidth += tickSize;
        }

        groupPositions = this._makePositions(seriesWidth);
        zeroTop = this._makeZeroTopForAddingData();

        this.graphRenderer.animateForAddingData(paramsForRendering, tickSize, groupPositions, shiftingOption, zeroTop);

        if (shiftingOption) {
            this._animateForMoving(tickSize);
        } else {
            this._animateForResizing(tickSize);
        }
    },

    /**
     * Cancel moving animation.
     * @private
     */
    _cancelMovingAnimation: function() {
        if (this.movingAnimation) {
            cancelAnimationFrame(this.movingAnimation.id);
            this.movingAnimation = null;
        }
    }
});

LineTypeSeriesBase.mixin = function(func) {
    tui.util.extend(func.prototype, LineTypeSeriesBase.prototype);
};

module.exports = LineTypeSeriesBase;
