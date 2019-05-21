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
name|project
operator|.
name|upgrade
operator|.
name|handlers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|DataChannelDescriptor
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|project
operator|.
name|upgrade
operator|.
name|UpgradeUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|UpgradeHandler_V7
implements|implements
name|UpgradeHandler
block|{
annotation|@
name|Override
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
literal|"7"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|processProjectDom
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
name|Element
name|domain
init|=
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|domain
operator|.
name|setAttribute
argument_list|(
literal|"project-version"
argument_list|,
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|Node
name|node
decl_stmt|;
try|try
block|{
name|node
operator|=
operator|(
name|Node
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/domain/property[@name='cayenne.DataDomain.usingExternalTransactions']"
argument_list|,
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return;
block|}
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|domain
operator|.
name|removeChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|processDataMapDom
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
name|Element
name|dataMap
init|=
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|setAttribute
argument_list|(
literal|"xmlns"
argument_list|,
literal|"http://cayenne.apache.org/schema/7/modelMap"
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setAttribute
argument_list|(
literal|"xsi:schemaLocation"
argument_list|,
literal|"http://cayenne.apache.org/schema/7/modelMap "
operator|+
literal|"https://cayenne.apache.org/schema/7/modelMap.xsd"
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setAttribute
argument_list|(
literal|"project-version"
argument_list|,
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|processModel
parameter_list|(
name|DataChannelDescriptor
name|dataChannelDescriptor
parameter_list|)
block|{
for|for
control|(
name|DataMap
name|dataMap
range|:
name|dataChannelDescriptor
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
comment|// if objEntity has super entity, then checks it for duplicated attributes
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|dataMap
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
name|ObjEntity
name|superEntity
init|=
name|objEntity
operator|.
name|getSuperEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|superEntity
operator|!=
literal|null
condition|)
block|{
name|removeShadowAttributes
argument_list|(
name|objEntity
argument_list|,
name|superEntity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Remove attributes from objEntity, if superEntity has attributes with same names.      */
specifier|private
name|void
name|removeShadowAttributes
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|,
name|ObjEntity
name|superEntity
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|delList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// if subAttr and superAttr have same names, adds subAttr to delList
for|for
control|(
name|ObjAttribute
name|subAttr
range|:
name|objEntity
operator|.
name|getDeclaredAttributes
argument_list|()
control|)
block|{
for|for
control|(
name|ObjAttribute
name|superAttr
range|:
name|superEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|subAttr
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|superAttr
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|delList
operator|.
name|add
argument_list|(
name|subAttr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|delList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|i
range|:
name|delList
control|)
block|{
name|objEntity
operator|.
name|removeAttribute
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

