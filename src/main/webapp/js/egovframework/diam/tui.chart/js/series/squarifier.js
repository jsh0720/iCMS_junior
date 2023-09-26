/**
 * @fileoverview squarifier create squarified bounds for rendering graph of treemap chart.
 * @author NHN Ent.
 *         FE Development Lab <dl_javascript@nhnent.com>
 */

'use strict';

var squarifier = {
    /**
     * bound map
     * @type {object.<string, {width: number, height: number, left: number, top: number}>}
     */
    boundMap: {},

    /**
     * Make base bound for calculating bounds.
     * @param {{width: number, height: number}} dimension - dimension
     * @returns {{width: number, height: number, left: number, top: number}}
     * @private
     */
    _makeBaseBound: function(dimension) {
        return tui.util.extend({
            left: 0,
            top: 0
        }, dimension);
    },

    /**
     * Calculate scale for calculating weight.
     * @param {Array.<number>} values - values
     * @param {number} width - width of series area
     * @param {number} height - height of series area
     * @returns {number}
     * @private
     */
    _calculateScale: function(values, width, height) {
        return (width * height) / tui.util.sum(values);
    },

    /**
     * Make base data for creating squarified bounds.
     * @param {Array.<SeriesItem>} seriesItems - SeriesItems
     * @param {number} width - width of series area
     * @param {number} height - height of series area
     * @returns {Array.<{itme: SeriesItem, weight: number}>}
     * @private
     */
    _makeBaseData: function(seriesItems, width, height) {
        var scale = this._calculateScale(tui.util.pluck(seriesItems, 'value'), width, height);
        var data = tui.util.map(seriesItems, function(seriesItem) {
            return {
                id: seriesItem.id,
                weight: seriesItem.value * scale
            };
        }).sort(function(a, b) {
            return b.weight - a.weight;
        });

        return data;
    },

    /**
     * Calculate worst aspect ratio.
     * Referred function worst() in https://www.win.tue.nl/~vanwijk/stm.pdf
     * @param {number} sum - sum for weights
     * @param {number} min - minimum weight
     * @param {number} max - maximum weight
     * @param {number} baseSize - base size (width or height)
     * @returns {number}
     * @private
     */
    _worst: function(sum, min, max, baseSize) {
        var sumSquare = sum * sum;
        var sizeSquare = baseSize * baseSize;

        return Math.max((sizeSquare * max) / sumSquare, sumSquare / (sizeSquare * min));
    },

    /**
     * Whether changed stack direction or not.
     * @param {number} sum - sum for weights
     * @param {Array.<number>} weights - weights
     * @param {number} baseSize - base size
     * @param {number} newWeight - new weight
     * @returns {boolean}
     * @private
     */
    _changedStackDirection: function(sum, weights, baseSize, newWeight) {
        var min = tui.util.min(weights);
        var max = tui.util.max(weights);
        var beforeWorst = this._worst(sum, min, max, baseSize);
        var newWorst = this._worst(sum + newWeight, Math.min(min, newWeight), Math.max(max, newWeight), baseSize);

        return newWorst >= beforeWorst;
    },

    /**
     * Whether type of vertical stack or not.
     * @param {{width: number, height: number}} baseBound - base bound
     * @returns {boolean}
     * @private
     */
    _isVerticalStack: function(baseBound) {
        return baseBound.height < baseBound.width;
    },

    /**
     * Select base size from baseBound.
     * @param {{width: number, height: number}} baseBound - base bound
     * @returns {number}
     * @private
     */
    _selectBaseSize: function(baseBound) {
        return this._isVerticalStack(baseBound) ? baseBound.height : baseBound.width;
    },

    /**
     * Calculate fixed size.
     * @param {number} baseSize - base size
     * @param {number} sum - sum for weights
     * @param {Array.<{weight: number}>} row - row
     * @returns {number}
     * @private
     */
    _calculateFixedSize: function(baseSize, sum, row) {
        var weights;

        if (!sum) {
            weights = tui.util.pluck(row, 'weight');
            sum = tui.util.sum(weights);
        }

        return sum / baseSize;
    },

    /**
     * Add bounds.
     * @param {number} startPosition - start position
     * @param {Array.<{weight: number}>} row - row
     * @param {number} fixedSize - fixed size
     * @param {function} callback - callback function
     * @private
     */
    _addBounds: function(startPosition, row, fixedSize, callback) {
        tui.util.reduce([startPosition].concat(row), function(storedPosition, rowDatum) {
            var dynamicSize = rowDatum.weight / fixedSize;

            callback(dynamicSize, storedPosition, rowDatum.id);

            return storedPosition + dynamicSize;
        });
    },

    /**
     * Add bound.
     * @param {number} left - left position
     * @param {number} top - top position
     * @param {number} width - width
     * @param {number} height - height
     * @param {string | number} id - id of seriesItem
     * @private
     */
    _addBound: function(left, top, width, height, id) {
        this.boundMap[id] = {
            left: left,
            top: top,
            width: width,
            height: height
        };
    },

    /**
     * Add bounds for type of vertical stack.
     * @param {Array.<{weight: number}>} row - row
     * @param {{left: number, top: number, width: number, height: number}} baseBound - base bound
     * @param {number} baseSize - base size
     * @param {number} sum - sum for weights of row
     * @private
     */
    _addBoundsForVerticalStack: function(row, baseBound, baseSize, sum) {
        var self = this;
        var fixedWidth = this._calculateFixedSize(baseSize, sum, row);

        this._addBounds(baseBound.top, row, fixedWidth, function(dynamicHeight, storedTop, id) {
            self._addBound(baseBound.left, storedTop, fixedWidth, dynamicHeight, id);
        });

        baseBound.left += fixedWidth;
        baseBound.width -= fixedWidth;
    },

    /**
     * Add bounds for type of horizontal stack.
     * @param {Array.<{weight: number}>} row - row
     * @param {{left: number, top: number, width: number, height: number}} baseBound - base bound
     * @param {number} baseSize - base size
     * @param {number} sum - sum for weights of row
     * @private
     */
    _addBoundsForHorizontalStack: function(row, baseBound, baseSize, sum) {
        var self = this;
        var fixedHeight = this._calculateFixedSize(baseSize, sum, row);

        this._addBounds(baseBound.left, row, fixedHeight, function(dynamicWidth, storedLeft, id) {
            self._addBound(storedLeft, baseBound.top, dynamicWidth, fixedHeight, id);
        });

        baseBound.top += fixedHeight;
        baseBound.height -= fixedHeight;
    },

    /**
     * Get adding bounds function.
     * @param {{width: number, height: number}} baseBound - base bound
     * @returns {*}
     * @private
     */
    _getAddingBoundsFunction: function(baseBound) {
        var addBound;

        if (this._isVerticalStack(baseBound)) {
            addBound = tui.util.bind(this._addBoundsForVerticalStack, this);
        } else {
            addBound = tui.util.bind(this._addBoundsForHorizontalStack, this);
        }

        return addBound;
    },

    /**
     * Create squarified bound map for graph rendering.
     * @param {{width: number, height: number}} dimension - dimension
     * @param {Array.<SeriesItem>} seriesItems - seriesItems
     * @returns {object.<string, {width: number, height: number, left: number, top: number}>}
     */
    squarify: function(dimension, seriesItems) {
        var self = this;
        var baseBound = this._makeBaseBound(dimension);
        var baseData = this._makeBaseData(seriesItems, baseBound.width, baseBound.height);
        var row = [];
        var baseSize, addBounds;

        this.boundMap = {};

        tui.util.forEachArray(baseData, function(datum) {
            var weights = tui.util.pluck(row, 'weight');
            var sum = tui.util.sum(weights);

            if (row.length && self._changedStackDirection(sum, weights, baseSize, datum.weight)) {
                addBounds(row, baseBound, baseSize, sum);
                row = [];
            }

            if (!row.length) {
                baseSize = self._selectBaseSize(baseBound);
                addBounds = self._getAddingBoundsFunction(baseBound);
            }

            row.push(datum);
        });

        if (row.length) {
            addBounds(row, baseBound, baseSize);
        }

        return this.boundMap;
    }
};

module.exports = squarifier;
