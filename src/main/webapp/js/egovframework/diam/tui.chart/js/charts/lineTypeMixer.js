/**
 * @fileoverview lineTypeMixer is mixer of line type chart(line, area).
 * @author NHN Ent.
 *         FE Development Lab <dl_javascript@nhnent.com>
 */

'use strict';

var ChartBase = require('./chartBase');
var AreaTypeCustomEvent = require('../customEvents/areaTypeCustomEvent');

/**
 * lineTypeMixer is mixer of line type chart(line, area).
 * @mixin
 */
var lineTypeMixer = {
    /**
     * Initialize line type chart.
     * @param {Array.<Array>} rawData raw data
     * @param {object} theme chart theme
     * @param {object} options chart options
     * @private
     */
    _lineTypeInit: function(rawData, theme, options) {
        ChartBase.call(this, {
            rawData: rawData,
            theme: theme,
            options: options,
            hasAxes: true,
            isVertical: true
        });

        this._addComponents(options.chartType);
    },

    /**
     * Make map for AxisScaleMaker of axes(xAxis, yAxis).
     * @returns {Object.<string, AxisScaleMaker>}
     * @private
     */
    _makeAxisScaleMakerMap: function() {
        return {
            yAxis: this._createAxisScaleMaker(this.options.yAxis, 'yAxis')
        };
    },

    /**
     * Add custom event component for normal tooltip.
     * @private
     */
    _addCustomEventComponentForNormalTooltip: function() {
        this.componentManager.register('customEvent', AreaTypeCustomEvent, {
            chartType: this.chartType,
            isVertical: this.isVertical,
            zoomable: tui.util.pick(this.options.series, 'zoomable')
        });
    },

    /**
     * Add components
     * @param {string} chartType chart type
     * @private
     */
    _addComponents: function(chartType) {
        this._addComponentsForAxisType({
            chartType: chartType,
            axis: [
                {
                    name: 'yAxis',
                    isVertical: true
                },
                {
                    name: 'xAxis',
                    isLabel: true
                }
            ],
            series: [
                {
                    name: this.options.chartType + 'Series',
                    SeriesClass: this.Series
                }
            ],
            plot: true
        });
    }
};

module.exports = lineTypeMixer;
