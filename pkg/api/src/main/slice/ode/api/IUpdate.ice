/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_API_IUPDATE_ICE
#define ODE_API_IUPDATE_ICE

#include <ode/cmd/API.ice>
#include <ode/ServicesF.ice>
#include <ode/Collections.ice>

module ode {

    module api {

        /**
         * Provides methods for directly updating object graphs. IUpdate is
         * the lowest level (level-1) interface which may make changes
         * (INSERT, UPDATE, DELETE) to the database. All other methods of
         * changing the database may leave it in an inconsistent state.
         *
         * <p>
         * All the save* methods act recursively on the entire object graph,
         * replacing placeholders and details where necessary, and then
         * <i>merging</i> the final graph.
         * This means that the objects that are passed into IUpdate.save*
         * methods are copied over to new instances which are then returned.
         * The original objects <b>should be discarded</b>.
         * </p>
         *
         * <p>{@code saveAndReturnIds} behaves slightly differently in that
         * it does <em>not</em> handle object modifications. The graph of
         * objects passed in can consist <em>ONLY</em> if either newly created
         * objects without ids or of unloaded objects with ids. <em>Note:</em>
         * The ids of the saved values may not be in order. This is caused by
         * persistence-by-transitivity. Hibernate may detect an item later in
         * the array if they are interconnected and therefore choose to save
         * it first.
         * </p>
         *
         * <p>
         * All methods throw {@link ode.ValidationException} if the input
         * objects do not pass validation, and
         * {@link ode.OptimisticLockException} if the version of a given has
         * already been incremented.
         */
        ["ami", "amd"] interface IUpdate extends ServiceInterface
            {
                void saveObject(ode::model::IObject obj) throws ServerError;
                void saveCollection(IObjectList objs) throws ServerError;
                ode::model::IObject saveAndReturnObject(ode::model::IObject obj) throws ServerError;
                void saveArray(IObjectList graph) throws ServerError;
                IObjectList saveAndReturnArray(IObjectList graph) throws ServerError;
                ode::sys::LongList saveAndReturnIds(IObjectList graph) throws ServerError;
                ["deprecate:use ode::cmd::Delete2 instead"]
                void deleteObject(ode::model::IObject row) throws ServerError;

                /**
                 * Initiates full-text indexing for the given object. This may
                 * have to wait
                 * for the current {@code FullTextThread} to finish.
                 * Can only be executed by an admin. Other users must wait for
                 * the background {@link Thread} to complete.
                 *
                 * @param row
                 *            a persistent {@link ode.model.IObject} to be deleted
                 * @throws ValidationException
                 *             if the object does not exist or is nul
                 */
                idempotent void indexObject(ode::model::IObject row) throws ServerError;
            };

        class Save extends ode::cmd::Request {
            ode::model::IObject obj;
        };

        class SaveRsp extends ode::cmd::Response {
            ode::model::IObject obj;
        };

    };
};

#endif