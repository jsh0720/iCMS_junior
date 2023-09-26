/**
 * @fileoverview Plot component.
 * @author NHN Ent.
 *         FE Development Lab <dl_javascript@nhnent.com>
 */

'use strict';

var dom = require('../helpers/domHandler'),
    calculator = require('../helpers/calculator'),
    renderUtil = require('../helpers/renderUtil'),
    plotTemplate = require('./plotTemplate');

var Plot = tui.util.defineClass(/** @lends Plot.prototype */ {
    /**
     * Plot component.
     * @constructs Plot
     * @param {object} params parameters
     *      @param {number} params.vTickCount vertical tick count
     *      @param {number} params.hTickCount horizontal tick count
     *      @param {object} params.theme axis theme
     */
    init: function(params) {
        /**
         * Plot view className
         * @type {string}
         */
        this.className = 'tui-chart-plot-area';

        /**
         * Bounds maker
         * @type {BoundsMaker}
         */
        this.boundsMaker = params.boundsMaker;

        /**
         * Options
         * @type {object}
         */
        this.options = params.options || {};
        this.options.showLine = tui.util.isUndefined(this.options.showLine) ? true : this.options.showLine;

        /**
         * Theme
         * @type {object}
         */
        this.theme = params.theme || {};

        /**
         * Plot data.
         * @type {object}
         */
        this.data = {};
    },

    /**
     * Render plot area.
     * @param {HTMLElement} plotContainer plot area element
     * @param {object} data rendering data
     * @private
     */
    _renderPlotArea: function(plotContainer, data) {
        var dimension = this.boundsMaker.getDimension('plot');
        this.data = data;

        renderUtil.renderDimension(plotContainer, dimension);
        renderUtil.renderPosition(plotContainer, this.boundsMaker.getPosition('plot'));

        if (this.options.showLine) {
            this._renderLines(plotContainer, dimension);
        }
    },

    /**
     * Render plot component.
     * @param {object} data rendering data
     * @returns {HTMLElement} plot element
     */
    render: function(data) {
        var container = dom.create('DIV', this.className);
        this._renderPlotArea(container, data);
        this.plotContainer = container;

        return container;
    },

    /**
     * Rerender.
     * @param {object} data rendering
     */
    rerender: function(data) {
        this.plotContainer.innerHTML = '';
        this._renderPlotArea(this.plotContainer, data);
    },

    /**
     * Resize plot component.
     * @param {object} data rendering data
     */
    resize: function(data) {
        this.rerender(data);
    },

    /**
     * Render plot lines.
     * @param {HTMLElement} el element
     * @param {{width: number, height: number}} dimension plot area dimension
     * @private
     */
    _renderLines: function(el, dimension) {
        var hPositions = this._makeHorizontalPixelPositions(dimension.width),
            vPositions = this._makeVerticalPixelPositions(dimension.height),
            theme = this.theme,
            lineHtml = '';

        lineHtml += this._makeLineHtml({
            positions: hPositions,
            size: dimension.height,
            className: 'vertical',
            positionType: 'left',
            sizeType: 'height',
            lineColor: theme.lineColor
        });
        lineHtml += this._makeLineHtml({
            positions: vPositions,
            size: dimension.width,
            className: 'horizontal',
            positionType: 'bottom',
            sizeType: 'width',
            lineColor: theme.lineColor
        });

        el.innerHTML = lineHtml;

        renderUtil.renderBackground(el, theme.background);
    },

    /**
     * Make html of plot line.
     * @param {object} params parameters
     *      @param {Array.<object>} params.positions positions
     *      @param {number} params.size width or height
     *      @param {string} params.className line className
     *      @param {string} params.positionType position type (left or bottom)
     *      @param {string} params.sizeType size type (size or height)
     *      @param {string} params.lineColor line color
     * @returns {string} html
     * @private
     */
    _makeLineHtml: function(params) {
        var template = plotTemplate.tplPlotLine;
        var lineHtml = tui.util.map(params.positions, function(position) {
            var cssTexts = [
                    renderUtil.concatStr(params.positionType, ':', position, 'px'),
                    renderUtil.concatStr(params.sizeType, ':', params.size, 'px')
                ], data;

            if (params.lineColor) {
                cssTexts.push(renderUtil.concatStr('background-color:', params.lineColor));
            }

            data = {className: params.className, cssText: cssTexts.join(';')};

            return template(data);
        }).join('');

        return lineHtml;
    },

    /**
     * Make pixel value of vertical positions
     * @param {number} height plot height
     * @returns {Array.<number>} positions
     * @private
     */
    _makeVerticalPixelPositions: function(height) {
        var positions = calculator.makeTickPixelPositions(height, this.data.vTickCount);

        positions.shift();

        return positions;
    },

    /**
     * Make divided positions of plot.
     * @param {number} width plot width
     * @returns {Array.<number>}
     * @private
     */
    _makeDividedPlotPositions: function(width) {
        var tickCount = parseInt(this.data.hTickCount / 2, 10) + 1,
            yAxisWidth = this.boundsMaker.getDimension('yAxis').width,
            leftWidth, rightWidth, leftPositions, rightPositions;

        width -= yAxisWidth;
        leftWidth = Math.round((width) / 2);
        rightWidth = width - leftWidth;

        leftPositions = calculator.makeTickPixelPositions(leftWidth, tickCount);
        rightPositions = calculator.makeTickPixelPositions(rightWidth, tickCount, leftWidth + yAxisWidth);

        leftPositions.pop();
        rightPositions.shift();

        return leftPositions.concat(rightPositions);
    },

    /**
     * Make pixel value of horizontal positions.
     * @param {number} width plot width
     * @returns {Array.<number>} positions
     * @private
     */
    _makeHorizontalPixelPositions: function(width) {
        var positions;

        if (this.options.divided) {
            positions = this._makeDividedPlotPositions(width);
        } else {
            positions = calculator.makeTickPixelPositions(width, this.data.hTickCount);
            positions.shift();
        }

        return positions;
    }
});

module.exports = Plot;
