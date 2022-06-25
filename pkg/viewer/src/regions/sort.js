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

export class SortValueConverter {

    // toView(arr, prop, ascending) {
    toView(rois, sortBy, sortAscending) {
        if (!rois) {
            return new Map();
        }
        // Convert Map to an Array of objects...
        let ids = Array.from(rois.keys());
        // Add id to each object so we know it after sorting
        let roiList = ids.map(id => {
            return Object.assign(rois.get(id), {id: id})
        });

        // default - sort by ROI ID
        let getAttr = (roi) => roi.id

        let getShapeText = (roi) => {
            if (!roi.shapes) return "";
            let label = "";
            for (let shape of roi.shapes.values()) {
                if (shape.Text && shape.Text.length > 0) {
                    label = shape.Text;
                    break;
                }
            }
            return label.toLowerCase();
        }

        let getNumberAttr = (attrName) => (roi) => {
            if (!roi.shapes) return -1;
            let val;
            // Return val of first shape
            for (let shape of roi.shapes.values()) {
                val = shape[attrName];
                break;
            }
            // Often -1 is used as a placeholder
            if (val === -1) return undefined;
            return val;
        }

        if (sortBy === 'shapeText') {
            getAttr = getShapeText;
        } else if (sortBy === 'theZ') {
            getAttr = getNumberAttr('TheZ');
        } else if (sortBy === 'theT') {
            getAttr = getNumberAttr('TheT');
        } else if (sortBy === 'area') {
            getAttr = getNumberAttr('Area')
        } else if (sortBy === 'length') {
            getAttr = getNumberAttr('Length')
        }

        let sorted = roiList.sort((a, b) => {
            let aValue = getAttr(a);
            let bValue = getAttr(b);
            // items with no value should go last
            if (!aValue && aValue !== 0) return sortAscending ? 1 : -1;
            if (!bValue && bValue !== 0) return sortAscending ? -1 : 1;
            if (aValue > bValue) return sortAscending ? 1 : -1;
            if (aValue < bValue) return sortAscending ? -1 : 1;
            return 0;
        });

        // Convert back to dictionary, inserting in the sorted order
        let orderedMap = new Map( sorted.map(r => [r.id, r]));

        return orderedMap;
    }
}