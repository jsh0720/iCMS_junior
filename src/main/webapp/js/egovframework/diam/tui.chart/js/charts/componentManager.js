/**
 * @fileoverview ComponentManager manages components of chart.
 * @author NHN Ent.
 *         FE Development Lab <dl_javascript@nhnent.com>
 */

'use strict';

var ComponentManager = tui.util.defineClass(/** @lends ComponentManager.prototype */ {
    /**
     * ComponentManager manages components of chart.
     * @param {object} params parameters
     *      @param {object} params.theme theme
     *      @param {object} params.options options
     *      @param {DataProcessor} params.dataProcessor data processor
     *      @param {BoundsMaker} params.boundsMaker bounds maker
     * @constructs ComponentManager
     */
    init: function(params) {
        /**
         * Components
         * @type {Array.<object>}
         */
        this.components = [];

        /**
         * Component map.
         * @type {object}
         */
        this.componentMap = {};

        /**
         * theme
         * @type {object}
         */
        this.theme = params.theme || {};

        /**
         * options
         * @type {object}
         */
        this.options = params.options || {};

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
         * whether chart has axes or not
         * @type {boolean}
         */
        this.hasAxes = params.hasAxes;
    },

    /**
     * Make component options.
     * @param {object} options options
     * @param {string} componentType component type
     * @param {number} index component index
     * @returns {object} options
     * @private
     */
    _makeComponentOptions: function(options, componentType, index) {
        options = options || this.options[componentType];
        options = tui.util.isArray(options) ? options[index] : options || {};

        return options;
    },

    /**
     * Register component.
     * The component refers to a component of the chart.
     * The component types are axis, legend, plot, series and customEvent.
     * Chart Component Description : https://i-msdn.sec.s-msft.com/dynimg/IC267997.gif
     * @param {string} name component name
     * @param {function} Component component constructor
     * @param {object} params component parameters
     */
    register: function(name, Component, params) {
        var index,
            component, componentType;

        params = params || {};

        componentType = params.componentType || name;
        index = params.index || 0;

        params.theme = params.theme || this.theme[componentType];
        params.options = this._makeComponentOptions(params.options, componentType, index);

        params.dataProcessor = this.dataProcessor;
        params.boundsMaker = this.boundsMaker;
        params.hasAxes = this.hasAxes;

        component = new Component(params);
        component.componentName = name;
        component.componentType = componentType;

        this.components.push(component);
        this.componentMap[name] = component;
    },

    /**
     * Iterate each components.
     * @param {function} iteratee iteratee
     */
    each: function(iteratee) {
        tui.util.forEachArray(this.components, iteratee);
    },

    /**
     * Return the results of applying the iteratee to each components.
     *  @param {function} iteratee iteratee
     * @returns {Array.<object>} components
     */
    map: function(iteratee) {
        return tui.util.map(this.components, iteratee);
    },

    /**
     * Find components to conditionMap.
     * @param {object} conditionMap condition map
     * @returns {Array.<object>} filtered components
     */
    where: function(conditionMap) {
        return tui.util.filter(this.components, function(component) {
            var contained = true;

            tui.util.forEach(conditionMap, function(value, key) {
                if (component[key] !== value) {
                    contained = false;
                }

                return contained;
            });

            return contained;
        });
    },

    /**
     * Get component.
     * @param {string} name component name
     * @returns {object} component instance
     */
    get: function(name) {
        return this.componentMap[name];
    }
});

module.exports = ComponentManager;
