/**
 * @fileoverview CustomEventBase is base class for event handle layers.
 * @author NHN Ent.
 *         FE Development Lab <dl_javascript@nhnent.com>
 */

'use strict';

var TickBaseCoordinateModel = require('./tickBaseCoordinateModel');
var BoundsBaseCoordinateModel = require('./boundsBaseCoordinateModel');
var chartConst = require('../const');
var eventListener = require('../helpers/eventListener');
var predicate = require('../helpers/predicate');
var dom = require('../helpers/domHandler');
var renderUtil = require('../helpers/renderUtil');

var CustomEventBase = tui.util.defineClass(/** @lends CustomEventBase.prototype */ {
    /**
     * CustomEventBase is base class for custom event components.
     * @constructs CustomEventBase
     * @param {object} params parameters
     *      @param {{
     *          dimension: {width: number, height: number},
     *          position: {left: number, top: number}
     *      }} params.bound bound
     *      @param {string} params.chartType chart type
     *      @param {boolean} params.isVertical whether vertical or not
     */
    init: function(params) {
        var isLineTypeChart;

        /**
         * type of chart
         * @type {string}
         */
        this.chartType = params.chartType;

        /**
         * chartTypes is available in combo chart
         * @type {Array.<string>}
         */
        this.chartTypes = params.chartTypes;

        /**
         * whether vertical or not
         * @type {boolean}
         */
        this.isVertical = params.isVertical;

        /**
         * data processor
         * @type {DataProcessor}
         */
        this.dataProcessor = params.dataProcessor;

        /**
         * bounds maker
         * @type {BoundsMaker}
         */
        this.boundsMaker = params.boundsMaker;

        /**
         * whether allow select series or not
         */
        this.allowSelect = params.allowSelect;

        /**
         * selected series item.
         * @type {null | object}
         */
        this.selectedData = null;

        /**
         * previous client position of mouse event (clientX, clientY)
         * @type {null | object}
         */
        this.prevClientPosition = null;

        /**
         * previous found data
         * @type {null | object}
         */
        this.prevFoundData = null;


        isLineTypeChart = predicate.isLineTypeChart(this.chartType, this.chartTypes);

        /**
         * expand size
         * @type {number}
         */
        this.expandSize = isLineTypeChart ? chartConst.SERIES_EXPAND_SIZE : 0;

        /**
         * container bound
         * @type {null | {left: number, top: number, right: number, bottom: number}}
         */
        this.containerBound = null;
    },

    /**
     * Get bound for rendering.
     * @returns {{
     *      dimension: {width: number, height: number},
     *      position: {left: number, top: number}
     * }}
     * @private
     */
    _getRenderingBound: function() {
        var renderingBound;

        if (predicate.isLineTypeChart(this.chartType, this.chartTypes)) {
            renderingBound = renderUtil.expandBound(this.boundsMaker.getBound('customEvent'));
        } else {
            renderingBound = this.boundsMaker.getBound('customEvent');
        }

        return renderingBound;
    },

    /**
     * Render event handle layer area.
     * @param {HTMLElement} customEventContainer - container element for custom event
     * @param {object} data - data for rendering
     * @private
     */
    _renderCustomEventArea: function(customEventContainer, data) {
        var dimension = this.boundsMaker.getDimension('customEvent');
        var renderingBound, tbcm;

        this.dimension = dimension;
        tbcm = new TickBaseCoordinateModel(dimension, data.tickCount, this.chartType, this.isVertical, this.chartTypes);
        this.tickBaseCoordinateModel = tbcm;
        renderingBound = this._getRenderingBound();
        renderUtil.renderDimension(customEventContainer, renderingBound.dimension);
        renderUtil.renderPosition(customEventContainer, renderingBound.position);
    },

    /**
     * Render for customEvent component.
     * @param {object} data - data for rendering
     * @returns {HTMLElement} container for custom event
     */
    render: function(data) {
        var container = dom.create('DIV', 'tui-chart-series-custom-event-area');

        this._renderCustomEventArea(container, data);
        this.attachEvent(container);
        this.customEventContainer = container;

        return container;
    },

    /**
     * Get container bound.
     * @returns {ClientRect}
     * @private
     */
    _getContainerBound: function() {
        if (!this.containerBound) {
            this.containerBound = this.customEventContainer.getBoundingClientRect();
        }

        return this.containerBound;
    },

    /**
     * Create BoundsBaseCoordinateModel from seriesBounds for custom event.
     * @param {Array.<object>} seriesBounds - series bounds
     */
    initCustomEventData: function(seriesBounds) {
        this.boundsBaseCoordinateModel = new BoundsBaseCoordinateModel(seriesBounds);
    },

    /**
     * Rerender for customEvent component.
     * @param {{tickCount: number}} data - data for rerendering
     */
    rerender: function(data) {
        this._renderCustomEventArea(this.customEventContainer, data);
    },

    /**
     * Resize for customEvent component.
     * @param {{tickCount: number}} data - data for resizing
     */
    resize: function(data) {
        this.containerBound = null;
        this.rerender(data);
    },

    /**
     * Whether changed select data or not.
     * @param {object} prev - previous data
     * @param {object} cur - current data
     * @returns {boolean}
     * @private
     */
    _isChangedSelectData: function(prev, cur) {
        return !prev || !cur || prev.chartType !== cur.chartType ||
            prev.indexes.groupIndex !== cur.indexes.groupIndex || prev.indexes.index !== cur.indexes.index;
    },

    /**
     * Find coordinate data from boundsCoordinateModel.
     * @param {HTMLElement} target - target element
     * @param {number} clientX mouse - position x
     * @param {number} clientY mouse - position y
     * @returns {object}
     * @private
     */
    _findDataFromBoundsCoordinateModel: function(target, clientX, clientY) {
        var bound = target.getBoundingClientRect();
        var layerX = clientX - bound.left;
        var layerY = clientY - bound.top;
        var groupIndex;

        if (predicate.isTreemapChart(this.chartType)) {
            groupIndex = 0;
        } else {
            groupIndex = this.tickBaseCoordinateModel.findIndex(this.isVertical ? layerX : layerY);
            layerX += chartConst.SERIES_EXPAND_SIZE;
            layerY += chartConst.SERIES_EXPAND_SIZE;
        }

        return this.boundsBaseCoordinateModel.findData(groupIndex, layerX, layerY);
    },

    /**
     * Unselect selected data.
     * @private
     */
    _unselectSelectedData: function() {
        var eventName = renderUtil.makeCustomEventName('unselect', this.selectedData.chartType, 'series');
        this.fire(eventName, this.selectedData);
        this.selectedData = null;
    },

    /**
     * Find data.
     * @private
     * @abstract
     */
    _findData: function() {},

    /**
     * Show tooltip
     * @private
     * @abstract
     */
    _showTooltip: function() {},

    /**
     * Animate for adding data.
     */
    animateForAddingData: function() {
        var foundData, isMoving;

        if (!this.prevClientPosition) {
            return;
        }

        foundData = this._findData(this.prevClientPosition.x, this.prevClientPosition.y);

        if (foundData) {
            isMoving = this.prevFoundData && (this.prevFoundData.indexes.groupIndex === foundData.indexes.groupIndex);
            this._showTooltip(foundData, isMoving);
        }

        this.prevFoundData = foundData;
    },

    /**
     * On mouse event.
     * @param {string} eventType - custom event type
     * @param {MouseEvent} e - mouse event
     * @private
     */
    _onMouseEvent: function(eventType, e) {
        var eventName = renderUtil.makeCustomEventName(eventType, this.chartType, 'series');

        dom.addClass(this.customEventContainer, 'hide');
        this.fire(eventName, {
            left: e.clientX,
            top: e.clientY
        });
        dom.removeClass(this.customEventContainer, 'hide');
    },

    /**
     * On click
     * @param {MouseEvent} e - mouse event
     * @private
     */
    _onClick: function(e) {
        var target = e.target || e.srcElement;
        var clientX = e.clientX - this.expandSize;
        var foundData = this._findDataFromBoundsCoordinateModel(target, clientX, e.clientY);

        if (!this._isChangedSelectData(this.selectedData, foundData)) {
            this._unselectSelectedData();
        } else if (foundData) {
            if (this.selectedData) {
                this._unselectSelectedData();
            }
            this.fire(renderUtil.makeCustomEventName('select', foundData.chartType, 'series'), foundData);

            if (this.allowSelect) {
                this.selectedData = foundData;
            }
        }
    },

    /**
     * On mouse down
     * @private
     * @abstract
     */
    _onMousedown: function() {},

    /**
     * On mouse up
     * @private
     * @abstract
     */
    _onMouseup: function() {},

    /**
     * On mouse move
     * @param {MouseEvent} e - mouse event
     * @private
     */
    _onMousemove: function(e) {
        this.prevClientPosition = {
            x: e.clientX,
            y: e.clientY
        };
    },

    /**
     * On mouse out
     * @private
     */
    _onMouseout: function() {
        this.prevClientPosition = null;
        this.prevFoundData = null;
    },

    /**
     * Attach event
     * @param {HTMLElement} target - target element
     */
    attachEvent: function(target) {
        eventListener.on(target, {
            click: this._onClick,
            mousedown: this._onMousedown,
            mouseup: this._onMouseup,
            mousemove: this._onMousemove,
            mouseout: this._onMouseout
        }, this);
    }
});

tui.util.CustomEvents.mixin(CustomEventBase);

module.exports = CustomEventBase;
