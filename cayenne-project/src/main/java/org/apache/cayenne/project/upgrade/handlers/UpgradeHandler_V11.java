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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|NoSuchFileException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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

begin_comment
comment|/**  * Upgrade handler for the project version "11" introduced by 4.3.M1 release.  * Changes highlight:  * - schemas version update  * - ROP removal  * - cgen schema changes  *  * @since 4.3  */
end_comment

begin_class
specifier|public
class|class
name|UpgradeHandler_V11
implements|implements
name|UpgradeHandler
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UpgradeHandler_V11
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|defaultTemplatePaths
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"templates/v4_1/singleclass.vm"
argument_list|,
literal|"templates/v4_1/superclass.vm"
argument_list|,
literal|"templates/v4_1/subclass.vm"
argument_list|,
literal|"templates/v4_1/embeddable-singleclass.vm"
argument_list|,
literal|"templates/v4_1/embeddable-superclass.vm"
argument_list|,
literal|"templates/v4_1/embeddable-subclass.vm"
argument_list|,
literal|"templates/v4_1/datamap-singleclass.vm"
argument_list|,
literal|"templates/v4_1/datamap-superclass.vm"
argument_list|,
literal|"templates/v4_1/datamap-subclass.vm"
argument_list|)
decl_stmt|;
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
name|updateTemplates
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
comment|/**      * upgrades templates in dataMap. Reads the file by path and write template if it was found.      * If not, the warning message will be written.      * In case of default template the path to default template will be written.      *      * @param upgradeUnit - unit to upgrade      */
specifier|private
name|void
name|updateTemplates
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
name|updateTemplate
argument_list|(
name|upgradeUnit
argument_list|,
literal|"template"
argument_list|)
expr_stmt|;
name|updateTemplate
argument_list|(
name|upgradeUnit
argument_list|,
literal|"superTemplate"
argument_list|)
expr_stmt|;
name|updateTemplate
argument_list|(
name|upgradeUnit
argument_list|,
literal|"embeddableTemplate"
argument_list|)
expr_stmt|;
name|updateTemplate
argument_list|(
name|upgradeUnit
argument_list|,
literal|"embeddableSuperTemplate"
argument_list|)
expr_stmt|;
name|updateTemplate
argument_list|(
name|upgradeUnit
argument_list|,
literal|"dataMapTemplate"
argument_list|)
expr_stmt|;
name|updateTemplate
argument_list|(
name|upgradeUnit
argument_list|,
literal|"dataMapSuperTemplate"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|updateTemplate
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|,
name|String
name|nodeName
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
name|templates
decl_stmt|;
try|try
block|{
name|templates
operator|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/data-map/*[local-name()='cgen']/*[local-name()='"
operator|+
name|nodeName
operator|+
literal|"']"
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
name|templates
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Node
name|node
init|=
name|templates
operator|.
name|item
argument_list|(
name|j
argument_list|)
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|String
name|dataMapPath
init|=
name|upgradeUnit
operator|.
name|getResource
argument_list|()
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|node
operator|.
name|setNodeValue
argument_list|(
name|readTemplateFile
argument_list|(
name|node
operator|.
name|getNodeValue
argument_list|()
argument_list|,
name|dataMapPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|readTemplateFile
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|dataMapPath
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isTemplateDefault
argument_list|(
name|path
argument_list|)
condition|)
block|{
try|try
block|{
name|String
name|templateFromClassPath
init|=
name|readTemplateFromClassPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|templateFromClassPath
operator|!=
literal|null
condition|)
block|{
return|return
name|templateFromClassPath
return|;
block|}
return|return
name|readTemplateFromFile
argument_list|(
name|path
argument_list|,
name|dataMapPath
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFileException
name|e
parameter_list|)
block|{
return|return
literal|"The template "
operator|+
name|path
operator|+
literal|" was not found during the project upgrade. Use the template editor in Cayenne modeler to set the template"
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Can't read the file: "
operator|+
name|path
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|"Can't read the file: "
operator|+
name|path
operator|+
name|e
operator|.
name|getMessage
argument_list|()
return|;
block|}
block|}
else|else
block|{
return|return
name|path
return|;
block|}
block|}
specifier|private
name|String
name|readTemplateFromClassPath
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|stream
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
init|)
block|{
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|String
argument_list|(
name|stream
operator|.
name|readAllBytes
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|readTemplateFromFile
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|dataMapPath
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|absolutPath
init|=
name|buildPath
argument_list|(
name|path
argument_list|,
name|dataMapPath
argument_list|)
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|Files
operator|.
name|readAllBytes
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|absolutPath
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|buildPath
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|dataMapPath
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|dataMap
init|=
operator|new
name|File
argument_list|(
name|dataMapPath
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|dataMap
operator|.
name|getParent
argument_list|()
argument_list|,
name|path
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|isTemplateDefault
parameter_list|(
name|String
name|template
parameter_list|)
block|{
return|return
name|defaultTemplatePaths
operator|.
name|contains
argument_list|(
name|template
argument_list|)
return|;
block|}
block|}
end_class

end_unit

