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
name|DeleteRule
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
name|ObjRelationship
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
name|ObjRelationshipHandler
extends|extends
name|NamespaceAwareNestedTagHandler
block|{
specifier|public
specifier|static
specifier|final
name|String
name|OBJ_RELATIONSHIP_TAG
init|=
literal|"obj-relationship"
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|DB_RELATIONSHIP_REF_TAG
init|=
literal|"db-relationship-ref"
decl_stmt|;
specifier|private
name|DataMap
name|map
decl_stmt|;
specifier|private
name|ObjRelationship
name|objRelationship
decl_stmt|;
specifier|public
name|ObjRelationshipHandler
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
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
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
name|OBJ_RELATIONSHIP_TAG
case|:
name|addObjRelationship
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
name|DB_RELATIONSHIP_REF_TAG
case|:
name|addDbRelationshipRef
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
comment|/**      *<db-relationship-ref> tag deprecated      */
annotation|@
name|Deprecated
specifier|private
name|void
name|addDbRelationshipRef
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
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
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"ObjRelationshipHandler::addDbRelationshipRef() - null DbRelationship name for "
operator|+
name|objRelationship
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|path
init|=
name|objRelationship
operator|.
name|getDbRelationshipPath
argument_list|()
decl_stmt|;
name|objRelationship
operator|.
name|setDbRelationshipPath
argument_list|(
operator|(
name|path
operator|!=
literal|null
operator|)
condition|?
name|path
operator|+
literal|"."
operator|+
name|name
else|:
name|name
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addObjRelationship
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
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
if|if
condition|(
literal|null
operator|==
name|name
condition|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"ObjRelationshipHandler::addObjRelationship() - unable to parse target."
argument_list|)
throw|;
block|}
name|String
name|sourceName
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"source"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"ObjRelationshipHandler::addObjRelationship() - unable to parse source."
argument_list|)
throw|;
block|}
name|ObjEntity
name|source
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
name|sourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"ObjRelationshipHandler::addObjRelationship() - unable to find source "
operator|+
name|sourceName
argument_list|)
throw|;
block|}
name|objRelationship
operator|=
operator|new
name|ObjRelationship
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|objRelationship
operator|.
name|setSourceEntity
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|objRelationship
operator|.
name|setTargetEntityName
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"target"
argument_list|)
argument_list|)
expr_stmt|;
name|objRelationship
operator|.
name|setDeleteRule
argument_list|(
name|DeleteRule
operator|.
name|deleteRuleForName
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"deleteRule"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|objRelationship
operator|.
name|setUsedForLocking
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
literal|"lock"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|objRelationship
operator|.
name|setDeferredDbRelationshipPath
argument_list|(
operator|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"db-relationship-path"
argument_list|)
operator|)
argument_list|)
expr_stmt|;
name|objRelationship
operator|.
name|setCollectionType
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"collection-type"
argument_list|)
argument_list|)
expr_stmt|;
name|objRelationship
operator|.
name|setMapKey
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"map-key"
argument_list|)
argument_list|)
expr_stmt|;
name|source
operator|.
name|addRelationship
argument_list|(
name|objRelationship
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjRelationship
name|getObjRelationship
parameter_list|()
block|{
return|return
name|objRelationship
return|;
block|}
block|}
end_class

end_unit

