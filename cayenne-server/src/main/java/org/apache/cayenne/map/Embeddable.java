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
name|io
operator|.
name|Serializable
import|;
end_import

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|configuration
operator|.
name|ConfigurationNode
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
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
name|cayenne
operator|.
name|util
operator|.
name|XMLSerializable
import|;
end_import

begin_comment
comment|/**  * A mapping descriptor of an embeddable class. Embeddable is a persistent class  * that doesn't have its own identity and is embedded in other persistent  * classes. It can be viewed as a custom type mapped to one or more database  * columns. Embeddable mapping can include optional default column names that  * can be overriden by the owning entity.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|Embeddable
implements|implements
name|ConfigurationNode
implements|,
name|XMLSerializable
implements|,
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7163768090567642099L
decl_stmt|;
specifier|protected
name|String
name|className
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|EmbeddableAttribute
argument_list|>
name|attributes
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|public
name|Embeddable
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Embeddable
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|attributes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
block|}
comment|/** 	 * @since 3.1 	 */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationNodeVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitEmbeddable
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
comment|/** 	 * Returns EmbeddableAttribute of this Embeddable that maps to 	 *<code>dbAttribute</code> parameter. Returns null if no such attribute is 	 * found. 	 */
specifier|public
name|EmbeddableAttribute
name|getAttributeForDbPath
parameter_list|(
name|String
name|dbPath
parameter_list|)
block|{
for|for
control|(
name|EmbeddableAttribute
name|attribute
range|:
name|attributes
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|dbPath
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getDbAttributeName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * Returns an unmodifiable sorted map of embeddable attributes. 	 */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|EmbeddableAttribute
argument_list|>
name|getAttributeMap
parameter_list|()
block|{
comment|// create a new instance ... Caching unmodifiable map causes
comment|// serialization issues (esp. with Hessian).
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|attributes
argument_list|)
return|;
block|}
comment|/** 	 * Returns an unmodifiable collection of embeddable attributes. 	 */
specifier|public
name|Collection
argument_list|<
name|EmbeddableAttribute
argument_list|>
name|getAttributes
parameter_list|()
block|{
comment|// create a new instance. Caching unmodifiable collection causes
comment|// serialization issues (esp. with Hessian).
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|attributes
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/** 	 * Adds new embeddable attribute to the entity, setting its parent 	 * embeddable to be this object. If attribute has no name, 	 * IllegalArgumentException is thrown. 	 */
specifier|public
name|void
name|addAttribute
parameter_list|(
name|EmbeddableAttribute
name|attribute
parameter_list|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attempt to insert unnamed attribute."
argument_list|)
throw|;
block|}
name|Object
name|existingAttribute
init|=
name|attributes
operator|.
name|get
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingAttribute
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|existingAttribute
operator|==
name|attribute
condition|)
block|{
return|return;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"An attempt to override embeddable attribute '"
operator|+
name|attribute
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
name|attributes
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setEmbeddable
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EmbeddableAttribute
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|attributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|attributes
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
specifier|public
name|void
name|setClassName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
block|}
comment|/** 	 * {@link XMLSerializable} implementation that generates XML for embeddable. 	 */
annotation|@
name|Override
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"embeddable"
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"className"
argument_list|,
name|getClassName
argument_list|()
argument_list|)
operator|.
name|nested
argument_list|(
name|attributes
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|visitEmbeddable
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

