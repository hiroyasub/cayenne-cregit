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
name|dba
operator|.
name|TypesMapping
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
name|event
operator|.
name|AttributeEvent
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
name|event
operator|.
name|DbAttributeListener
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

begin_comment
comment|/**  * A DbAttribute defines a descriptor for a single database table column.  *   * @author Misha Shengaout  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DbAttribute
extends|extends
name|Attribute
block|{
comment|/**      * Defines JDBC type of the column.      */
specifier|protected
name|int
name|type
init|=
name|TypesMapping
operator|.
name|NOT_DEFINED
decl_stmt|;
comment|/**      * Defines whether the attribute allows nulls.      */
specifier|protected
name|boolean
name|mandatory
decl_stmt|;
comment|/**      * Defines whether the attribute is a part of the table primary key.      */
specifier|protected
name|boolean
name|primaryKey
decl_stmt|;
comment|/**      * Defines whether this column value is generated by the database. Other terms for      * such columns are "auto-increment" or "identity".      *       * @since 1.2      */
specifier|protected
name|boolean
name|generated
decl_stmt|;
comment|// The length of CHAR or VARCHAr or max num of digits for DECIMAL.
specifier|protected
name|int
name|maxLength
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * @since 3.0      */
specifier|protected
name|int
name|scale
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * @since 3.0      */
comment|// must call it 'attributePrecison' as 'precision' in 1.2 really meant 'scale'
specifier|protected
name|int
name|attributePrecision
init|=
operator|-
literal|1
decl_stmt|;
specifier|public
name|DbAttribute
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|DbAttribute
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
name|DbAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|type
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
block|{
name|this
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints itself as XML to the provided XMLEncoder.      *       * @since 1.1      */
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
literal|"<db-attribute name=\""
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
name|getName
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
name|String
name|type
init|=
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" type=\""
operator|+
name|type
operator|+
literal|'\"'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" isPrimaryKey=\"true\""
argument_list|)
expr_stmt|;
comment|// only allow generated if an attribute is a PK.
if|if
condition|(
name|isGenerated
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" isGenerated=\"true\""
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isMandatory
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" isMandatory=\"true\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getMaxLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" length=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getMaxLength
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
name|getScale
argument_list|()
operator|>
literal|0
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" scale=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getScale
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
name|getAttributePrecision
argument_list|()
operator|>
literal|0
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" attributePrecision=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getAttributePrecision
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
name|encoder
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAliasedName
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
return|return
operator|(
name|alias
operator|!=
literal|null
operator|)
condition|?
name|alias
operator|+
literal|'.'
operator|+
name|this
operator|.
name|getName
argument_list|()
else|:
name|this
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * Returns the SQL type of the column.      *       * @see java.sql.Types      */
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Sets the SQL type for the column.      *       * @see java.sql.Types      */
specifier|public
name|void
name|setType
parameter_list|(
name|int
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
specifier|public
name|boolean
name|isPrimaryKey
parameter_list|()
block|{
return|return
name|primaryKey
return|;
block|}
comment|/**      * Returns<code>true</code> if the DB column represented by this attribute is a      * foreign key, referencing another table.      *       * @since 1.1      */
specifier|public
name|boolean
name|isForeignKey
parameter_list|()
block|{
name|String
name|name
init|=
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
comment|// won't be able to match joins...
return|return
literal|false
return|;
block|}
name|Iterator
name|relationships
init|=
name|getEntity
argument_list|()
operator|.
name|getRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|relationships
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|relationship
init|=
operator|(
name|DbRelationship
operator|)
name|relationships
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|relationship
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
condition|)
block|{
name|DbAttribute
name|target
init|=
name|join
operator|.
name|getTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
operator|&&
name|target
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Updates attribute "primaryKey" property.      */
specifier|public
name|void
name|setPrimaryKey
parameter_list|(
name|boolean
name|primaryKey
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|primaryKey
operator|!=
name|primaryKey
condition|)
block|{
name|this
operator|.
name|primaryKey
operator|=
name|primaryKey
expr_stmt|;
name|Entity
name|e
init|=
name|this
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|DbAttributeListener
condition|)
block|{
operator|(
operator|(
name|DbAttributeListener
operator|)
name|e
operator|)
operator|.
name|dbAttributeChanged
argument_list|(
operator|new
name|AttributeEvent
argument_list|(
name|this
argument_list|,
name|this
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|isMandatory
parameter_list|()
block|{
return|return
name|mandatory
return|;
block|}
specifier|public
name|void
name|setMandatory
parameter_list|(
name|boolean
name|mandatory
parameter_list|)
block|{
name|this
operator|.
name|mandatory
operator|=
name|mandatory
expr_stmt|;
block|}
comment|/**      * Returns the length of database column described by this attribute.      */
specifier|public
name|int
name|getMaxLength
parameter_list|()
block|{
return|return
name|maxLength
return|;
block|}
comment|/**      * Sets the length of character or binary type or max num of digits for DECIMAL.      */
specifier|public
name|void
name|setMaxLength
parameter_list|(
name|int
name|maxLength
parameter_list|)
block|{
name|this
operator|.
name|maxLength
operator|=
name|maxLength
expr_stmt|;
block|}
comment|/**      * Returns the number of digits after period for DECIMAL.      *       * @deprecated since 3.0 as this property really referred to 'scale'. Use      *             {@link #getScale()} instead.      */
specifier|public
name|int
name|getPrecision
parameter_list|()
block|{
return|return
name|getScale
argument_list|()
return|;
block|}
comment|/**      * @deprecated since 3.0 as this property really referred to 'scale'. Use      *             {@link #setScale(int)} instead.      */
specifier|public
name|void
name|setPrecision
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|setScale
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if this column value is generated by the database. Other terms for      * such columns are "auto-increment" or "identity".      *       * @since 1.2      */
specifier|public
name|boolean
name|isGenerated
parameter_list|()
block|{
return|return
name|generated
return|;
block|}
comment|/**      * Updates attribute "generated" property.      *       * @since 1.2      */
specifier|public
name|void
name|setGenerated
parameter_list|(
name|boolean
name|generated
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|generated
operator|!=
name|generated
condition|)
block|{
name|this
operator|.
name|generated
operator|=
name|generated
expr_stmt|;
name|Entity
name|e
init|=
name|this
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|DbAttributeListener
condition|)
block|{
operator|(
operator|(
name|DbAttributeListener
operator|)
name|e
operator|)
operator|.
name|dbAttributeChanged
argument_list|(
operator|new
name|AttributeEvent
argument_list|(
name|this
argument_list|,
name|this
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @since 3.0      */
specifier|public
name|int
name|getAttributePrecision
parameter_list|()
block|{
return|return
name|attributePrecision
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setAttributePrecision
parameter_list|(
name|int
name|attributePrecision
parameter_list|)
block|{
name|this
operator|.
name|attributePrecision
operator|=
name|attributePrecision
expr_stmt|;
block|}
comment|/**      * Returns the number of digits after period for decimal attributes. Returns "-1" if      * not set.      *       * @since 3.0      */
specifier|public
name|int
name|getScale
parameter_list|()
block|{
return|return
name|scale
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setScale
parameter_list|(
name|int
name|scale
parameter_list|)
block|{
name|this
operator|.
name|scale
operator|=
name|scale
expr_stmt|;
block|}
block|}
end_class

end_unit

