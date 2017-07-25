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
name|configuration
operator|.
name|xml
package|;
end_package

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
name|exp
operator|.
name|ExpressionFactory
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
name|DataMap
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
name|DbAttribute
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
name|xml
operator|.
name|sax
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ContentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|DbEntityHandler
extends|extends
name|NamespaceAwareNestedTagHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DB_ENTITY_TAG
init|=
literal|"db-entity"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DB_ATTRIBUTE_TAG
init|=
literal|"db-attribute"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DB_KEY_GENERATOR_TAG
init|=
literal|"db-key-generator"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|QUALIFIER_TAG
init|=
literal|"qualifier"
decl_stmt|;
specifier|private
name|DataMap
name|dataMap
decl_stmt|;
specifier|private
name|DbEntity
name|entity
decl_stmt|;
specifier|private
name|DbAttribute
name|lastAttribute
decl_stmt|;
specifier|public
name|DbEntityHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parentHandler
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
name|super
argument_list|(
name|parentHandler
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|processElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|DB_ENTITY_TAG
case|:
name|createDbEntity
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
name|DB_ATTRIBUTE_TAG
case|:
name|createDbAttribute
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
name|QUALIFIER_TAG
case|:
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processCharData
parameter_list|(
name|String
name|localName
parameter_list|,
name|String
name|data
parameter_list|)
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|QUALIFIER_TAG
case|:
name|createQualifier
argument_list|(
name|data
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|ContentHandler
name|createChildTagHandler
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|DB_KEY_GENERATOR_TAG
case|:
return|return
operator|new
name|DbKeyGeneratorHandler
argument_list|(
name|this
argument_list|,
name|entity
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|createChildTagHandler
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|,
name|attributes
argument_list|)
return|;
block|}
specifier|private
name|void
name|createDbEntity
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|String
name|name
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|entity
operator|=
operator|new
name|DbEntity
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setSchema
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"schema"
argument_list|)
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setCatalog
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"catalog"
argument_list|)
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|addDbEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createDbAttribute
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|String
name|name
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|lastAttribute
operator|=
operator|new
name|DbAttribute
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|lastAttribute
operator|.
name|setType
argument_list|(
name|TypesMapping
operator|.
name|getSqlTypeByName
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
name|entity
operator|.
name|addAttribute
argument_list|(
name|lastAttribute
argument_list|)
expr_stmt|;
name|String
name|length
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"length"
argument_list|)
decl_stmt|;
if|if
condition|(
name|length
operator|!=
literal|null
condition|)
block|{
name|lastAttribute
operator|.
name|setMaxLength
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|length
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|precision
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"attributePrecision"
argument_list|)
decl_stmt|;
if|if
condition|(
name|precision
operator|!=
literal|null
condition|)
block|{
name|lastAttribute
operator|.
name|setAttributePrecision
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|precision
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// this is an obsolete 1.2 'precision' attribute that really meant 'scale'
name|String
name|pseudoPrecision
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"precision"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pseudoPrecision
operator|!=
literal|null
condition|)
block|{
name|lastAttribute
operator|.
name|setScale
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|pseudoPrecision
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|scale
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"scale"
argument_list|)
decl_stmt|;
if|if
condition|(
name|scale
operator|!=
literal|null
condition|)
block|{
name|lastAttribute
operator|.
name|setScale
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|scale
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|lastAttribute
operator|.
name|setPrimaryKey
argument_list|(
name|DataMapHandler
operator|.
name|TRUE
operator|.
name|equalsIgnoreCase
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"isPrimaryKey"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|lastAttribute
operator|.
name|setMandatory
argument_list|(
name|DataMapHandler
operator|.
name|TRUE
operator|.
name|equalsIgnoreCase
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"isMandatory"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|lastAttribute
operator|.
name|setGenerated
argument_list|(
name|DataMapHandler
operator|.
name|TRUE
operator|.
name|equalsIgnoreCase
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"isGenerated"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createQualifier
parameter_list|(
name|String
name|qualifier
parameter_list|)
block|{
if|if
condition|(
name|qualifier
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
comment|// qualifier can belong to ObjEntity, DbEntity or a query
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|entity
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|qualifier
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|DbEntity
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
specifier|public
name|DbAttribute
name|getLastAttribute
parameter_list|()
block|{
return|return
name|lastAttribute
return|;
block|}
block|}
end_class

end_unit

