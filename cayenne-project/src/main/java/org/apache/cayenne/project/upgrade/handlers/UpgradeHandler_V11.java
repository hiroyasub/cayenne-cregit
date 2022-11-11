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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * Upgrade handler for the project version "11" introduced by 5.0.M1 release.  * Changes highlight:  *      - schemas version update  *      - ROP removal  *      - cgen schema changes  *  * @since 5.0  */
end_comment

begin_class
specifier|public
class|class
name|UpgradeHandler_V11
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
literal|"11"
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
name|updateDomainSchemaAndVersion
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
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
name|updateDataMapSchemaAndVersion
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
name|updateExtensionSchema
argument_list|(
name|upgradeUnit
argument_list|,
literal|"cgen"
argument_list|)
expr_stmt|;
name|updateExtensionSchema
argument_list|(
name|upgradeUnit
argument_list|,
literal|"dbimport"
argument_list|)
expr_stmt|;
name|updateExtensionSchema
argument_list|(
name|upgradeUnit
argument_list|,
literal|"graph"
argument_list|)
expr_stmt|;
name|upgradeComments
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
name|dropROPProperties
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
name|dropObjEntityClientInfo
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
name|updateCgenConfig
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|upgradeComments
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
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
name|NodeList
name|infoNodes
decl_stmt|;
try|try
block|{
name|infoNodes
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"//*[local-name()='property']"
argument_list|,
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|infoNodes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|infoElement
init|=
operator|(
name|Element
operator|)
name|infoNodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
if|if
condition|(
name|infoElement
operator|.
name|hasAttribute
argument_list|(
literal|"xmlns:info"
argument_list|)
condition|)
block|{
name|infoElement
operator|.
name|setAttribute
argument_list|(
literal|"xmlns:info"
argument_list|,
literal|"http://cayenne.apache.org/schema/11/info"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|dropROPProperties
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
name|NodeList
name|propertyNodes
decl_stmt|;
try|try
block|{
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
name|propertyNodes
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/data-map/property"
argument_list|,
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
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
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|propertyNodes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|propertyElement
init|=
operator|(
name|Element
operator|)
name|propertyNodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|propertyElement
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|name
condition|)
block|{
case|case
literal|"clientSupported"
case|:
case|case
literal|"defaultClientPackage"
case|:
case|case
literal|"defaultClientSuperclass"
case|:
name|dataMap
operator|.
name|removeChild
argument_list|(
name|propertyElement
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|private
name|void
name|dropObjEntityClientInfo
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
name|NodeList
name|objEntityNodes
decl_stmt|;
try|try
block|{
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
name|objEntityNodes
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/data-map/obj-entity"
argument_list|,
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
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
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|objEntityNodes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|objEntityElement
init|=
operator|(
name|Element
operator|)
name|objEntityNodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|objEntityElement
operator|.
name|removeAttribute
argument_list|(
literal|"serverOnly"
argument_list|)
expr_stmt|;
name|objEntityElement
operator|.
name|removeAttribute
argument_list|(
literal|"clientClassName"
argument_list|)
expr_stmt|;
name|objEntityElement
operator|.
name|removeAttribute
argument_list|(
literal|"clientSuperClassName"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateCgenConfig
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
name|renameQueryTemplates
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
name|dropCgenClientConfig
argument_list|(
name|upgradeUnit
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|renameQueryTemplates
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
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
name|NodeList
name|queryTemplates
decl_stmt|;
name|NodeList
name|querySuperTemplates
decl_stmt|;
try|try
block|{
name|queryTemplates
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/data-map/*[local-name()='cgen']/*[local-name()='queryTemplate']"
argument_list|,
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
expr_stmt|;
name|querySuperTemplates
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/data-map/*[local-name()='cgen']/*[local-name()='querySuperTemplate']"
argument_list|,
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return;
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|queryTemplates
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Node
name|element
init|=
name|queryTemplates
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
operator|.
name|renameNode
argument_list|(
name|element
argument_list|,
literal|null
argument_list|,
literal|"dataMapTemplate"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|querySuperTemplates
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Node
name|element
init|=
name|querySuperTemplates
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
operator|.
name|renameNode
argument_list|(
name|element
argument_list|,
literal|null
argument_list|,
literal|"dataMapSuperTemplate"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|dropCgenClientConfig
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
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
name|NodeList
name|nodes
decl_stmt|;
try|try
block|{
name|nodes
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/data-map/*[local-name()='cgen']/*[local-name()='client']"
argument_list|,
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return;
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|element
init|=
operator|(
name|Element
operator|)
name|nodes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|element
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

