// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import {noView} from 'aurelia-framework';
import Misc from '../utils/misc';

/**
 * Keeps track of object changes to be able to undo/redo.
 * The idea is to make any class that needs a 'history' extend this one,
 * and then call record on it handing it the properties that have changed
 * as well as the old values (for redo). This way we keep only track of what has
 * changed.
 */
@noView
export default class History {
    /**
     * @memberof History
     * @type {boolean}
     */
    debug = true;

    /**
     * a flag that determines whether undo/redo are enabled
     * @memberof History
     * @type {boolean}
     */
    undo_redo_enabled = true;

    /**
     * @memberof History
     * @type {Array.<Object>}
     */
    history = [];

    /**
     * @memberof History
     * @type {number}
     */
    historyPointer = -1;

    /**
     * Records an action that relates to a property remembering its old value,
     * it's new value and, optionally (but highly recommended), the type that
     * the property is.
     * Note that the root for the property is the instance of the class
     * that we extended with History ('this') hence the need to for extension
     * In other words if you specify prop: ['bla', 'hey'], the 'fully qualified'
     * property name will be: this.bla.hey. Should this property not exist in the
     * actual instance, we fail silently or emit a debug message if the debug flag
     * is set to true
     *
     * @param {Object|Array.<Object>} record an object (see default value) or array of objects
     * @memberof History
     */
     addHistory(record =
            {scope: null, prop: ['null'], old_val: null, new_val: null, type: 'object'}) {
         let entries = [];
         if (Misc.isArray(record)) entries = record;
         else entries.push(record);
         if (entries.length === 0) return;

         // loop over entries
        entries.map((action) => {
             // we conduct some preliminary checks to see if we received
             // what was expected and mandatory
             if (typeof action !== 'object' || action === null ||
                    !Misc.isArray(action.prop) || action.prop.length === 0) {
                if (this.debug) console.debug(
                    "History.record requires an action object pointing to a property!");
                return;
             }

             // check old_val, new_val in regards to its type
             let t =
                typeof action.type === 'string' && action.type.length > 0 ?
                    action.type : 'undefined'
             let actT = typeof action.old_val;
             if ((t !== 'undefined' && actT !== t) ||
                    t === 'undefined' && actT === t) {
                if (this.debug) console.debug(
                    "History.record requires an action object with old_val and new_val ");
                return;
             }

             if (!this.checkProperty(action.prop, action.scope)) {
                 if (this.debug) console.debug(
                     "History.record: given property does not exist!");
                 return;
             }
         });
         // add entries now
         this.history.splice(
             this.historyPointer+1,
             this.history.length-this.historyPointer, entries);
         this.historyPointer++;
     }

    /**
     * At a minimum this method checks whether a property for the given strings
     * exists or not. If a callback function is given as the second parameter
     * it can be used to set the value of the property for instance (or do other stuff)
     * @private
     * @param {Array.<string>} path an array of strings determining the 'path' to the property
     * @param {Object=} scope an optional scope, otherwise this is assumed
     * @param {function=} callback an optional callback function
     *
     * @memberof History
     * @return {boolean} true if the property was found, false otherwise
    */
    checkProperty(path = [], scope = null, callback = null) {
        if (!Misc.isArray(path) || path.length === 0) return false;

        scope = scope ? scope : this;
        let accPath = scope;
        let lastBit = null;
        for (let i=0;i<path.length;i++) {
            // path is empty or not a string
            if (typeof path[i] !== 'string' || path[i].length === 0) return false;
            if (i>0) accPath = accPath[path[i-1]]; // accumulate the path
            // check if the new addition is defined
            if (typeof accPath[path[i]] === 'undefined') return false;
            lastBit = path[i];
        }
        if (typeof callback === 'function') callback.call(scope, accPath, lastBit);
        return true;
    }

    /**
     * Undoes the last action
     * @memberof History
     */
    undoHistory() {
        if (!this.canUndo()) return;

        let entries = this.history[this.historyPointer];
        entries.map(action => {
            let undo = (path, lastBit) => path[lastBit] = action.old_val;
            if (!this.checkProperty(action.prop, action.scope, undo)) {
                if (this.debug) console.debug("Failed to undo history");
                return;
        }});
        //adjust pointer
        this.historyPointer--;
    }

    /**
     * Redoes the last action
     * @memberof History
     */
     redoHistory() {
         if (!this.canRedo()) return;

         let entries = this.history[this.historyPointer+1];
         entries.map(action => {
             let redo = (path, lastBit) => path[lastBit] = action.new_val;
             if (!this.checkProperty(action.prop, action.scope, redo)) {
                 if (this.debug) console.debug("Failed to redo history");
                 return;
         }});
         //adjust pointer
         this.historyPointer++;
     }

      /**
       * @return {boolean} true if we are not at the end of the history
       * @memberof History
       */
       canRedo() {
           return this.hasHistory() && this.undo_redo_enabled &&
                        this.historyPointer < this.history.length-1;
       }

       /**
        * @return {boolean} true if we are not at the beginning of the history
        * @memberof History
        */
        canUndo() {
            return this.hasHistory() && this.undo_redo_enabled &&
                        this.historyPointer >= 0;
        }

      /**
       * @return {boolean} true if we have at least one record, false otherwise
       * @memberof History
       */
       hasHistory() {
           return this.history.length > 0;
       }

      /**
       * Resets history
       * @memberof History
       */
       resetHistory() {
           this.history = [];
           this.historyPointer = -1;
       }
}