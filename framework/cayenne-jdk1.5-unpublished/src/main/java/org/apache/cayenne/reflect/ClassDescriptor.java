begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * A runtime descriptor of an persistent class.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|ClassDescriptor
block|{
comment|/**      * Returns an ObjEntity associated with this descriptor.      *       * @since 3.0      */
name|ObjEntity
name|getEntity
parameter_list|()
function_decl|;
comment|/**      * Returns a class mapped by this descriptor.      */
name|Class
name|getObjectClass
parameter_list|()
function_decl|;
comment|/**      * Returns a descriptor of the mapped superclass or null if the descriptor's entity      * sits at the top of inheritance hierarchy or no inheritance is mapped.      */
name|ClassDescriptor
name|getSuperclassDescriptor
parameter_list|()
function_decl|;
comment|/**      * Returns the most "specialized" descriptor for a given class. This method assumes      * that the following is true:      *       *<pre>      * this.getObjectClass().isAssignableFrom(objectClass)      *</pre>      */
name|ClassDescriptor
name|getSubclassDescriptor
parameter_list|(
name|Class
name|objectClass
parameter_list|)
function_decl|;
comment|/**      * Creates a new instance of a class described by this object.      */
name|Object
name|createObject
parameter_list|()
function_decl|;
comment|/**      * Prepares object properties for access. This may include injection of value holders      * into the object and such.      */
name|void
name|injectValueHolders
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * Merges object properties from one object to another, avoiding traversal of the      * ArcProperties.      */
name|void
name|shallowMerge
parameter_list|(
name|Object
name|from
parameter_list|,
name|Object
name|to
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * Returns a property descriptor matching property name, or null if no such property      * is found. Lookup includes properties from this descriptor and all its superclass      * decsriptors. Returned property can be any one of {@link AttributeProperty},      * {@link ToManyProperty}, {@link ToOneProperty}.      */
name|Property
name|getProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
function_decl|;
comment|/**      * Returns a Java Bean property descriptor matching property name or null if no such      * property is found. Lookup DOES NOT including properties from the superclass      * descriptors. Returned property can be any one of {@link AttributeProperty},      * {@link ToManyProperty}, {@link ToOneProperty}.      */
name|Property
name|getDeclaredProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
function_decl|;
comment|/**      * Returns an Iterator over descriptor properties.      *       * @deprecated since 3.0. Use {@link #visitProperties(PropertyVisitor)} method      *             instead.      */
name|Iterator
name|getProperties
parameter_list|()
function_decl|;
comment|/**      * Returns an iterator over the properties mapped to id columns.      *       * @since 3.0      */
name|Iterator
name|getIdProperties
parameter_list|()
function_decl|;
comment|/**      * Returns an iterator over the arc properties whose reverse arcs are to-many maps.      * I.e. for each ArcProperty in the iterator, the following is true:      *       *<pre>      * arc.getComplimentaryReverseArc() instanceof ToManyMapProperty      *</pre>      *       * @since 3.0      */
name|Iterator
name|getMapArcProperties
parameter_list|()
function_decl|;
comment|/**      * Passes the visitor to all properties "visit" method, terminating properties      * walkthrough in case one of the properties returns false. Returns true if all      * visited properties returned true, false - if one property returned false.      */
name|boolean
name|visitProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Passes the visitor to the properties "visit" method for all properties declared in      * this descriptor, terminating properties walkthrough in case one of the properties      * returns false. Returns true if all visited properties returned true, false - if one      * property returned false.      *       * @since 3.0      */
name|boolean
name|visitDeclaredProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Passes the visitor to the properties "visit" method for all properties declared in      * this descriptor, its super and subdescriptors, terminating properties walkthrough      * in case one of the properties returns false. Returns true if all visited properties      * returned true, false - if one property returned false.      *       * @since 3.0      */
name|boolean
name|visitAllProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Returns true if an object is not fully resolved.      */
name|boolean
name|isFault
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

