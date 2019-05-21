begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
package|;
end_package

begin_comment
comment|/**  * Defines basic methods for a persistent object in Cayenne.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataObject
extends|extends
name|Persistent
block|{
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_VERSION
init|=
name|Long
operator|.
name|MIN_VALUE
decl_stmt|;
comment|/**      * Modifies a value of a named property without altering the object state in any way,      * and without triggering any database operations. This method is intended mostly for      * internal use by Cayenne framework, and shouldn't be called from the application      * code.      */
specifier|public
name|void
name|writePropertyDirectly
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Object
name|val
parameter_list|)
function_decl|;
comment|/**      * Returns mapped property value as curently stored in the DataObject. Returned value      * maybe a fault or a real value. This method will not attempt to resolve faults, or      * to read unmapped properties.      */
specifier|public
name|Object
name|readPropertyDirectly
parameter_list|(
name|String
name|propertyName
parameter_list|)
function_decl|;
comment|/**      * Returns a value of the property identified by a property path. Supports reading      * both mapped and unmapped properties. Unmapped properties are accessed in a manner      * consistent with JavaBeans specification.      *<p>      * Property path (or nested property) is a dot-separated path used to traverse object      * relationships until the final object is found. If a null object found while      * traversing path, null is returned. If a list is encountered in the middle of the      * path, CayenneRuntimeException is thrown. Unlike      * {@link #readPropertyDirectly(String)}, this method will resolve an object if it is      * HOLLOW.      *<p>      * Examples:      *</p>      *<ul>      *<li>Read this object property:<br>      *<code>String name = (String)artist.readNestedProperty("name");</code><br>      *<br>      *</li>      *<li>Read an object related to this object:<br>      *<code>Gallery g = (Gallery)paintingInfo.readNestedProperty("toPainting.toGallery");</code>      *<br>      *<br>      *</li>      *<li>Read a property of an object related to this object:<br>      *<code>String name = (String)painting.readNestedProperty("toArtist.artistName");</code>      *<br>      *<br>      *</li>      *<li>Read to-many relationship list:<br>      *<code>List exhibits = (List)painting.readNestedProperty("toGallery.exhibitArray");</code>      *<br>      *<br>      *</li>      *<li>Read to-many relationship in the middle of the path:<br>      *<code>List&lt;String&gt; names = (List&lt;String&gt;)artist.readNestedProperty("paintingArray.paintingName");</code>      *<br>      *<br>      *</li>      *</ul>      *       * @since 1.0.5      */
specifier|public
name|Object
name|readNestedProperty
parameter_list|(
name|String
name|path
parameter_list|)
function_decl|;
comment|/**      * Returns a value of the property identified by propName. Resolves faults if needed.      * This method can safely be used instead of or in addition to the auto-generated      * property accessors in subclasses of CayenneDataObject.      */
specifier|public
name|Object
name|readProperty
parameter_list|(
name|String
name|propName
parameter_list|)
function_decl|;
comment|/**      * Sets the property to the new value. Resolves faults if needed. This method can be      * safely used instead of or in addition to the auto-generated property modifiers to      * set simple properties. Note that to set to-one relationships use      * {@link #setToOneTarget(String, DataObject, boolean)}.      *       * @param propertyName a name of the bean property being modified.      * @param value a new value of the property.      */
specifier|public
name|void
name|writeProperty
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Adds an object to a to-many relationship.      */
specifier|public
name|void
name|addToManyTarget
parameter_list|(
name|String
name|relationshipName
parameter_list|,
name|DataObject
name|target
parameter_list|,
name|boolean
name|setReverse
parameter_list|)
function_decl|;
comment|/**      * Removes an object from a to-many relationship.      */
specifier|public
name|void
name|removeToManyTarget
parameter_list|(
name|String
name|relationshipName
parameter_list|,
name|DataObject
name|target
parameter_list|,
name|boolean
name|unsetReverse
parameter_list|)
function_decl|;
comment|/**      * Sets to-one relationship to a new value. Resolves faults if needed. This method can      * safely be used instead of or in addition to the auto-generated property modifiers      * to set properties that are to-one relationships.      *       * @param relationshipName a name of the bean property being modified - same as the      *            name of ObjRelationship.      * @param value a new value of the property.      * @param setReverse whether to update the reverse relationship pointing from the old      *            and new values of the property to this object.      */
specifier|public
name|void
name|setToOneTarget
parameter_list|(
name|String
name|relationshipName
parameter_list|,
name|DataObject
name|value
parameter_list|,
name|boolean
name|setReverse
parameter_list|)
function_decl|;
comment|/**      * Returns a version of a DataRow snapshot that was used to create this object.      *       * @since 1.1      */
specifier|public
name|long
name|getSnapshotVersion
parameter_list|()
function_decl|;
comment|/**      * @since 1.1      */
specifier|public
name|void
name|setSnapshotVersion
parameter_list|(
name|long
name|snapshotVersion
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

