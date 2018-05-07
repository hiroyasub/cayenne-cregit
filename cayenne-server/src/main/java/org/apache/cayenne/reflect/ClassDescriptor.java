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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|exp
operator|.
name|Expression
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
name|DbEntity
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
name|EntityInheritanceTree
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
name|ObjAttribute
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
comment|/**  * A runtime descriptor of an persistent class.  *   * @since 1.2  */
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
comment|/**      * Returns a collection of DbEntities that are the root tables for this descriptor's      * ObjEntity. Usually such collection would contain only one entity, however in cases      * involving subclass horizontal inheritance, it will be more than one, and in cases      * of abstract entities with no subclasses, the collection will be empty.      *       * @since 3.0      */
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|getRootDbEntities
parameter_list|()
function_decl|;
comment|/**      * Returns information about additional db entities that is used for this ObjEntity (i.e. for flattened attributes).      *<p>      * Keys are full paths for corresponding flattened attributes.      *<p>      *      * @since 4.1      * @return information about additional db entities      */
name|Map
argument_list|<
name|String
argument_list|,
name|DbEntity
argument_list|>
name|getAdditionalDbEntities
parameter_list|()
function_decl|;
comment|/**      * @since 3.0      */
name|EntityInheritanceTree
name|getEntityInheritanceTree
parameter_list|()
function_decl|;
comment|/**      * Returns whether this class has persistent subclasses.      *       * @since 3.1      */
name|boolean
name|hasSubclasses
parameter_list|()
function_decl|;
comment|/**      * Returns a class mapped by this descriptor.      */
name|Class
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
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
comment|/**      * Returns a property descriptor matching property name, or null if no such property      * is found. Lookup includes properties from this descriptor and all its superclass      * descriptors. Returned property can be any one of {@link AttributeProperty},      * {@link ToManyProperty}, {@link ToOneProperty}.      */
name|PropertyDescriptor
name|getProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
function_decl|;
comment|/**      * Returns a Java Bean property descriptor matching property name or null if no such      * property is found. Lookup DOES NOT including properties from the superclass      * descriptors. Returned property can be any one of {@link AttributeProperty},      * {@link ToManyProperty}, {@link ToOneProperty}.      */
name|PropertyDescriptor
name|getDeclaredProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
function_decl|;
comment|/**      * Returns a collection of the properties mapped to id columns.      *       * @since 3.1      */
name|Collection
argument_list|<
name|AttributeProperty
argument_list|>
name|getIdProperties
parameter_list|()
function_decl|;
comment|/**      * Returns a collection of ObjAttribute for the described class, its superclasses and      * subclasses, that participate in inheritance qualifier. If a discriminator      * expression specifies a DbAttribute instead of an ObjAttribute, a synthetic      * ObjAttribute is created and returned.      *       * @since 3.1      */
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|getDiscriminatorColumns
parameter_list|()
function_decl|;
comment|/**      * Returns entity qualifier as a Cayenne expression that includes qualifiers for this      * entity and all subentities.      *       * @since 3.0      */
name|Expression
name|getEntityQualifier
parameter_list|()
function_decl|;
comment|/**      * Returns a collection over the arc properties whose reverse arcs are to-many maps.      * I.e. for each ArcProperty in returned collection, the following is true:      *       *<pre>      * arc.getComplimentaryReverseArc() instanceof ToManyMapProperty      *</pre>      *       * @since 3.1      */
name|Collection
argument_list|<
name|ArcProperty
argument_list|>
name|getMapArcProperties
parameter_list|()
function_decl|;
comment|/**      * Passes the visitor to the properties "visit" method for all properties declared in      * this descriptor and all its super-decsriptors. Properties that are overridden in      * subdescriptors will include overridden information. Walkthrough is terminated in      * case one of the properties returns false. Returns true if all visited properties      * returned true, false - if one property returned false.      */
name|boolean
name|visitProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Passes the visitor to the properties "visit" method for all properties declared in      * this descriptor. This property set excludes inherited properties, even those that      * got overridden in this subclass. Walkthrough is terminated in case one of the      * properties returns false. Returns true if all visited properties returned true,      * false - if one property returned false.      *       * @since 3.0      */
name|boolean
name|visitDeclaredProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Passes the visitor to the properties "visit" method for a combination of all      * properties, including properties declared in this descriptor, its super      * descriptors, and all subdescriptors. Walkthrough is terminated in case one of the      * properties returns false. Returns true if all visited properties returned true,      * false - if one property returned false.      *       * @since 3.0      */
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

