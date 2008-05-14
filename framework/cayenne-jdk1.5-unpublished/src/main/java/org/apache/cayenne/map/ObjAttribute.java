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
name|map
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
name|CayenneRuntimeException
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
name|util
operator|.
name|CayenneMapEntry
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
name|util
operator|.
name|Util
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
name|util
operator|.
name|XMLEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|IteratorUtils
import|;
end_import

begin_comment
comment|/**  * An ObjAttribute is a mapping descriptor of a Java class property.  *   * @author Misha Shengaout  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ObjAttribute
extends|extends
name|Attribute
block|{
specifier|protected
name|String
name|type
decl_stmt|;
specifier|protected
name|boolean
name|usedForLocking
decl_stmt|;
specifier|protected
name|String
name|dbAttributePath
decl_stmt|;
specifier|public
name|ObjAttribute
parameter_list|()
block|{
block|}
specifier|public
name|ObjAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|ObjEntity
name|entity
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns Java class of an object property described by this attribute. Wraps any      * thrown exceptions into CayenneRuntimeException.      */
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getJavaClass
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|getType
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|Util
operator|.
name|getJavaClass
argument_list|(
name|getType
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Failed to load class for name '"
operator|+
name|this
operator|.
name|getType
argument_list|()
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Prints itself as XML to the provided XMLEncoder.      *       * @since 1.1      */
annotation|@
name|Override
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<obj-attribute name=\""
operator|+
name|getName
argument_list|()
operator|+
literal|'\"'
argument_list|)
expr_stmt|;
if|if
condition|(
name|getType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" type=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isUsedForLocking
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" lock=\"true\""
argument_list|)
expr_stmt|;
block|}
comment|// If this obj attribute is mapped to db attribute
if|if
condition|(
name|getDbAttribute
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" db-attribute-path=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|Util
operator|.
name|encodeXmlAttribute
argument_list|(
name|getDbAttributePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns fully qualified Java class name of the object property represented by this      * attribute.      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Sets the type of the data object property. Type is expected to be a fully qualified      * Java class name.      */
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|boolean
name|isPrimaryKey
parameter_list|()
block|{
if|if
condition|(
name|dbAttributePath
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|DbAttribute
name|dbAttribute
init|=
name|getDbAttribute
argument_list|()
decl_stmt|;
return|return
name|dbAttribute
operator|!=
literal|null
operator|&&
name|dbAttribute
operator|.
name|isPrimaryKey
argument_list|()
return|;
block|}
comment|/**      * Returns whether this attribute should be used for locking.      *       * @since 1.1      */
specifier|public
name|boolean
name|isUsedForLocking
parameter_list|()
block|{
return|return
name|usedForLocking
return|;
block|}
comment|/**      * Sets whether this attribute should be used for locking.      *       * @since 1.1      */
specifier|public
name|void
name|setUsedForLocking
parameter_list|(
name|boolean
name|usedForLocking
parameter_list|)
block|{
name|this
operator|.
name|usedForLocking
operator|=
name|usedForLocking
expr_stmt|;
block|}
comment|/**      * Returns a DbAttribute mapped by this ObjAttribute.      */
specifier|public
name|DbAttribute
name|getDbAttribute
parameter_list|()
block|{
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|pathIterator
init|=
name|getDbPathIterator
argument_list|()
decl_stmt|;
name|CayenneMapEntry
name|o
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|pathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|o
operator|=
name|pathIterator
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|DbAttribute
operator|)
name|o
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|getDbPathIterator
parameter_list|()
block|{
if|if
condition|(
name|dbAttributePath
operator|==
literal|null
condition|)
block|{
return|return
name|IteratorUtils
operator|.
name|EMPTY_ITERATOR
return|;
block|}
name|ObjEntity
name|ent
init|=
operator|(
name|ObjEntity
operator|)
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|ent
operator|==
literal|null
condition|)
block|{
return|return
name|IteratorUtils
operator|.
name|EMPTY_ITERATOR
return|;
block|}
name|DbEntity
name|dbEnt
init|=
name|ent
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEnt
operator|==
literal|null
condition|)
block|{
return|return
name|IteratorUtils
operator|.
name|EMPTY_ITERATOR
return|;
block|}
name|int
name|lastPartStart
init|=
name|dbAttributePath
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastPartStart
operator|<
literal|0
condition|)
block|{
name|DbAttribute
name|attribute
init|=
operator|(
name|DbAttribute
operator|)
name|dbEnt
operator|.
name|getAttribute
argument_list|(
name|dbAttributePath
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|==
literal|null
condition|)
block|{
return|return
name|IteratorUtils
operator|.
name|EMPTY_ITERATOR
return|;
block|}
return|return
name|IteratorUtils
operator|.
name|singletonIterator
argument_list|(
name|attribute
argument_list|)
return|;
block|}
return|return
name|dbEnt
operator|.
name|resolvePathComponents
argument_list|(
name|dbAttributePath
argument_list|)
return|;
block|}
comment|/**      * Set mapped DbAttribute.      *       * @deprecated since 3.0 - this method only works for non-flattened attributes.      *             'setDbAttributePath' should be used instead.      */
specifier|public
name|void
name|setDbAttribute
parameter_list|(
name|DbAttribute
name|dbAttribute
parameter_list|)
block|{
if|if
condition|(
name|dbAttribute
operator|==
literal|null
condition|)
block|{
name|setDbAttributePath
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setDbAttributePath
argument_list|(
name|dbAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the the name of the mapped DbAttribute. This value is the same as      * "dbAttributePath" for regular attributes mapped to columns. It is equql to the last      * path component for the flattened attributes.      */
specifier|public
name|String
name|getDbAttributeName
parameter_list|()
block|{
if|if
condition|(
name|dbAttributePath
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|lastDot
init|=
name|dbAttributePath
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastDot
operator|<
literal|0
condition|)
block|{
return|return
name|dbAttributePath
return|;
block|}
return|return
name|dbAttributePath
operator|.
name|substring
argument_list|(
name|lastDot
operator|+
literal|1
argument_list|)
return|;
block|}
comment|/**      * Sets the name of the mapped DbAttribute.      *       * @deprecated since 3.0 use {@link #setDbAttributePath(String)}.      */
specifier|public
name|void
name|setDbAttributeName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setDbAttributePath
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDbAttributePath
parameter_list|(
name|String
name|dbAttributePath
parameter_list|)
block|{
name|this
operator|.
name|dbAttributePath
operator|=
name|dbAttributePath
expr_stmt|;
block|}
comment|/**      * Returns a dot-separated path that starts in the root DbEntity that maps to this      * attribute's ObjEntity and spans zero or more relationships, always ending in a      * DbAttribute name.      */
specifier|public
name|String
name|getDbAttributePath
parameter_list|()
block|{
return|return
name|dbAttributePath
return|;
block|}
comment|/**      * @deprecated since 3.0 use 'isFlattened'.      */
specifier|public
name|boolean
name|isCompound
parameter_list|()
block|{
return|return
name|isFlattened
argument_list|()
return|;
block|}
comment|/**      * Returns whether this attribute is "flattened", meaning that it points to a column      * from an entity other than the DbEntity mapped to the parent ObjEntity.      *       * @since 3.0      */
specifier|public
name|boolean
name|isFlattened
parameter_list|()
block|{
return|return
name|dbAttributePath
operator|!=
literal|null
operator|&&
name|dbAttributePath
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>=
literal|0
return|;
block|}
comment|/**      * Returns an ObjAttribute stripped of any server-side information, such as      * DbAttribute mapping.      *       * @since 1.2      */
specifier|public
name|ObjAttribute
name|getClientAttribute
parameter_list|()
block|{
name|ClientObjAttribute
name|attribute
init|=
operator|new
name|ClientObjAttribute
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|attribute
operator|.
name|setType
argument_list|(
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|dbAttribute
init|=
name|getDbAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAttribute
operator|!=
literal|null
condition|)
block|{
comment|// expose PK attribute names - the client may need those to build ObjectIds
if|if
condition|(
name|dbAttribute
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|attribute
operator|.
name|setDbAttributePath
argument_list|(
name|dbAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|attribute
operator|.
name|setMandatory
argument_list|(
name|dbAttribute
operator|.
name|isMandatory
argument_list|()
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setMaxLength
argument_list|(
name|dbAttribute
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: will likely need "userForLocking" property as well.
return|return
name|attribute
return|;
block|}
block|}
end_class

end_unit

