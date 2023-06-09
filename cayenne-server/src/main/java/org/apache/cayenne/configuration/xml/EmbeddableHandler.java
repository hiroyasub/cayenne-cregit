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
name|Embeddable
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
name|EmbeddableAttribute
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
name|SAXException
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|EmbeddableHandler
extends|extends
name|NamespaceAwareNestedTagHandler
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EMBEDDABLE_TAG
init|=
literal|"embeddable"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EMBEDDABLE_ATTRIBUTE_TAG
init|=
literal|"embeddable-attribute"
decl_stmt|;
specifier|private
name|DataMap
name|map
decl_stmt|;
specifier|private
name|Embeddable
name|embeddable
decl_stmt|;
specifier|public
name|EmbeddableHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parentHandler
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|super
argument_list|(
name|parentHandler
argument_list|)
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|map
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
name|EMBEDDABLE_TAG
case|:
name|createEmbeddable
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
name|EMBEDDABLE_ATTRIBUTE_TAG
case|:
name|createEmbeddableAttribute
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|createEmbeddable
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|embeddable
operator|=
operator|new
name|Embeddable
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"className"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|addEmbeddable
argument_list|(
name|embeddable
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createEmbeddableAttribute
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|EmbeddableAttribute
name|ea
init|=
operator|new
name|EmbeddableAttribute
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
argument_list|)
decl_stmt|;
name|ea
operator|.
name|setType
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"type"
argument_list|)
argument_list|)
expr_stmt|;
name|ea
operator|.
name|setDbAttributeName
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"db-attribute-name"
argument_list|)
argument_list|)
expr_stmt|;
name|embeddable
operator|.
name|addAttribute
argument_list|(
name|ea
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Embeddable
name|getEmbeddable
parameter_list|()
block|{
return|return
name|embeddable
return|;
block|}
block|}
end_class

end_unit

