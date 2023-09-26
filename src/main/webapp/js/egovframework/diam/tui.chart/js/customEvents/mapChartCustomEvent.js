/**
 * @fileoverview MapChartCustomEvent is event handle layer for map chart.
 * @author NHN Ent.
 *         FE Development Lab <dl_javascript@nhnent.com>
 */

'use strict';

var CustomEventBase = require('./customEventBase'),
    chartConst = require('../const'),
    eventListener = require('../helpers/eventListener'),
    dom = require('../helpers/domHandler'),
    renderUtil = require('../helpers/renderUtil');

var MapChartCustomEvent = tui.util.defineClass(CustomEventBase, /** @lends MapChartCustomEvent.prototype */ {
    /**
     * MapChartCustomEvent is event handle layer for map chart.
     * @param {object} params parameters
     *      @param {BoundsMaker} params.boundsMaker boundsMaker instance
     * @constructs MapChartCustomEvent
     * @extends CustomEventBase
     */
    init: function(params) {
        this.boundsMaker = params.boundsMaker;
        this.chartType = params.chartType;
        this.isDown = false;
    },
    /**
     * Render event handle layer area
     * @param {HTMLElement} customEventContainer custom event container element
     * @private
     */
    _renderCustomEventArea: function(customEventContainer) {
        var bound = this.boundsMaker.getBound('customEvent');
        renderUtil.renderDimension(customEventContainer, bound.dimension);
        renderUtil.renderPosition(customEventContainer, bound.position);
    },

    /**
     * Initialize data of custom event
     * @override
     */
    initCustomEventData: function() {},

    /**
     * On click.
     * @private
     * @override
     */
    _onClick: function() {},

    /**
     * On mouse down
     * @param {mouseevent} e mouse event
     * @private
     * @override
     */
    _onMousedown: function(e) {
        this.isDown = true;
        this.fire('dragStartMapSeries', {
            left: e.clientX,
            top: e.clientY
        });
    },

    /**
     * Drag end.
     * @private
     */
    _dragEnd: function() {
        this.isDrag = false;
        dom.removeClass(this.customEventContainer, 'drag');
        this.fire('dragEndMapSeries');
    },

    /**
     * On mouse up
     * @param {mouseevent} e mouse event
     * @private
     * @override
     */
    _onMouseup: function(e) {
        this.isDown = false;

        if (this.isDrag) {
            this._dragEnd();
        } else {
            this._onMouseEvent('click', e);
        }

        this.isMove = false;
    },

    /**
     * On mouse move.
     * @param {mouseevent} e mouse event
     * @private
     * @override
     */
    _onMousemove: function(e) {
        if (this.isDown) {
            if (!this.isDrag) {
                dom.addClass(this.customEventContainer, 'drag');
            }
            this.isDrag = true;
            this.fire('dragMapSeries', {
                left: e.clientX,
                top: e.clientY
            });
        } else {
            this.isMove = true;
            this._onMouseEvent('move', e);
        }
    },

    /**
     * On mouse out
     * @private
     * @override
     */
    _onMouseout: function(e) {
        if (this.isDrag) {
            this._dragEnd();
        } else {
            this._onMouseEvent('move', e);
        }
        this.isDown = false;
    },

    /**
     * On mouse wheel.
     * @param {mouseevent} e mouse event
     * @returns {?boolean}
     * @private
     */
    _onMousewheel: function(e) {
        var wheelDelta = e.wheelDelta || e.detail * chartConst.FF_WHEELDELTA_ADJUSTING_VALUE;

        this.fire('wheel', wheelDelta, {
            left: e.clientX,
            top: e.clientY
        });

        if (e.preventDefault) {
            e.preventDefault();
        }

        return false;
    },

    /**
     * Attach event
     * @param {HTMLElement} target target element
     * @override
     */
    attachEvent: function(target) {
        CustomEventBase.prototype.attachEvent.call(this, target);

        if (tui.util.browser.firefox) {
            eventListener.on(target, 'DOMMouseScroll', this._onMousewheel, this);
        } else {
            eventListener.on(target, 'mousewheel', this._onMousewheel, this);
        }
    }
});

tui.util.CustomEvents.mixin(MapChartCustomEvent);

module.exports = MapChartCustomEvent;
