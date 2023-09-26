/**
 * @fileoverview TickBaseDataModel is tick base data model.
 * @author NHN Ent.
 *         FE Development Lab <dl_javascript@nhnent.com>
 */

'use strict';

var predicate = require('../helpers/predicate');

var TickBaseDataModel = tui.util.defineClass(/** @lends TickBaseDataModel.prototype */ {
    /**
     * TickBaseDataModel is tick base data model.
     * @param {{width: number, height: number}} dimension dimension
     * @param {number} tickCount tick count
     * @param {string} chartType chart type
     * @param {boolean} isVertical whether vertical or not
     * @param {Array.<string>} [chartTypes] - chart types of combo chart
     * @constructs TickBaseDataModel
     */
    init: function(dimension, tickCount, chartType, isVertical, chartTypes) {
        /**
         * whether line type or not
         * @type {boolean}
         */
        this.isLineType = predicate.isLineTypeChart(chartType, chartTypes);

        this.data = this._makeData(dimension, tickCount, isVertical);
    },

    /**
     * Make tick base data about line type chart.
     * @param {number} width width
     * @param {number} tickCount tick count
     * @returns {Array} tick base data
     * @private
     */
    _makeLineTypeData: function(width, tickCount) {
        var tickInterval = (width + 1) / (tickCount - 1),
            halfInterval = tickInterval / 2,
            ranges = tui.util.map(tui.util.range(0, tickCount), function(index) {
                return {
                    min: index * tickInterval - halfInterval,
                    max: index * tickInterval + halfInterval
                };
            });
        ranges[tickCount - 1].max -= 1;

        return ranges;
    },

    /**
     * Make tick base data about non line type chart.
     * @param {number} size width or height
     * @param {number} tickCount tick count
     * @returns {Array} tick base data
     * @private
     */
    _makeNormalData: function(size, tickCount) {
        var len = tickCount - 1;
        var tickInterval = size / len;
        var prev = 0;

        return tui.util.map(tui.util.range(0, len), function(index) {
            var max = tui.util.min([size, (index + 1) * tickInterval]);
            var limit = {
                min: prev,
                max: max
            };
            prev = max;

            return limit;
        });
    },

    /**
     * Make tick base data for custom event.
     * @param {{width: number, height: number}} dimension dimension
     * @param {number} tickCount tick count
     * @param {boolean} isVertical whether vertical or not
     * @returns {Array.<object>} tick base data
     * @private
     */
    _makeData: function(dimension, tickCount, isVertical) {
        var sizeType = isVertical ? 'width' : 'height';
        var data;

        if (this.isLineType) {
            data = this._makeLineTypeData(dimension[sizeType], tickCount);
        } else {
            data = this._makeNormalData(dimension[sizeType], tickCount);
        }

        return data;
    },

    /**
     * Find index.
     * @param {number} pointValue mouse position point value
     * @returns {number} group index
     */
    findIndex: function(pointValue) {
        var foundIndex = -1;

        tui.util.forEachArray(this.data, function(limit, index) {
            if (limit.min < pointValue && limit.max >= pointValue) {
                foundIndex = index;

                return false;
            }

            return true;
        });

        return foundIndex;
    },

    /**
     * Get last index.
     * @returns {number}
     */
    getLastIndex: function() {
        return this.data.length - 1;
    },

    /**
     * Make range of tooltip position.
     * @param {number} index index
     * @param {string} chartType chart type
     * @returns {{start: number, end: number}} range type value
     * @private
     */
    makeRange: function(index) {
        var limit = this.data[index],
            range, center;
        if (this.isLineType) {
            center = parseInt(limit.max - (limit.max - limit.min) / 2, 10);
            range = {
                start: center,
                end: center
            };
        } else {
            range = {
                start: limit.min,
                end: limit.max
            };
        }

        return range;
    }
});

module.exports = TickBaseDataModel;
