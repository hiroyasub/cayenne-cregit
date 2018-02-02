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
name|project
operator|.
name|upgrade
package|;
end_package

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
name|net
operator|.
name|URL
import|;
end_import

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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
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
name|ConfigurationException
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
name|ConfigurationTree
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
name|configuration
operator|.
name|DataChannelDescriptorLoader
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
name|di
operator|.
name|Inject
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
name|Project
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
name|ProjectSaver
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
name|handlers
operator|.
name|UpgradeHandler
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
name|resource
operator|.
name|Resource
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
name|Document
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
name|InputSource
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

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|XMLReader
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
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
operator|.
name|isBlank
import|;
end_import

begin_comment
comment|/**  *  * Upgrade service sequence is following:  * 1. This cycle should be done by Modeler and will result in a full project upgrade  *  *  - find all project and datamap resources  *  - define set of upgrade handlers to process those resources  *  - process DOM (project + N data maps)  *  - save& load cycle to flush all DOM changes  *  - process project model  *  - save once again to cleanup and sort final XML  *  * 2. This cycle can be used by ServerRuntime to optionally support old project versions  *  *  - find all project and datamap resources  *  - define set of upgrade handlers to process those resources  *  - process DOM (project + N data maps)  *  - directly load model from DOM w/o saving  *  - process project model  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultUpgradeService
implements|implements
name|UpgradeService
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
name|DefaultUpgradeService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNKNOWN_VERSION
init|=
literal|"0"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MIN_SUPPORTED_VERSION
init|=
literal|"6"
decl_stmt|;
name|TreeMap
argument_list|<
name|String
argument_list|,
name|UpgradeHandler
argument_list|>
name|handlers
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|VersionComparator
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ProjectSaver
name|projectSaver
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataChannelDescriptorLoader
name|loader
decl_stmt|;
specifier|public
name|DefaultUpgradeService
parameter_list|(
annotation|@
name|Inject
name|List
argument_list|<
name|UpgradeHandler
argument_list|>
name|handlerList
parameter_list|)
block|{
for|for
control|(
name|UpgradeHandler
name|handler
range|:
name|handlerList
control|)
block|{
name|handlers
operator|.
name|put
argument_list|(
name|handler
operator|.
name|getVersion
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|UpgradeMetaData
name|getUpgradeType
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|UpgradeMetaData
name|metaData
init|=
operator|new
name|UpgradeMetaData
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|loadProjectVersion
argument_list|(
name|resource
argument_list|)
decl_stmt|;
name|metaData
operator|.
name|setProjectVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|metaData
operator|.
name|setSupportedVersion
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Project
operator|.
name|VERSION
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|c1
init|=
name|VersionComparator
operator|.
name|INSTANCE
operator|.
name|compare
argument_list|(
name|version
argument_list|,
name|MIN_SUPPORTED_VERSION
argument_list|)
decl_stmt|;
if|if
condition|(
name|c1
operator|<
literal|0
condition|)
block|{
name|metaData
operator|.
name|setIntermediateUpgradeVersion
argument_list|(
name|MIN_SUPPORTED_VERSION
argument_list|)
expr_stmt|;
name|metaData
operator|.
name|setUpgradeType
argument_list|(
name|UpgradeType
operator|.
name|INTERMEDIATE_UPGRADE_NEEDED
argument_list|)
expr_stmt|;
return|return
name|metaData
return|;
block|}
name|int
name|c2
init|=
name|VersionComparator
operator|.
name|INSTANCE
operator|.
name|compare
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Project
operator|.
name|VERSION
argument_list|)
argument_list|,
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|c2
operator|<
literal|0
condition|)
block|{
name|metaData
operator|.
name|setUpgradeType
argument_list|(
name|UpgradeType
operator|.
name|DOWNGRADE_NEEDED
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|c2
operator|==
literal|0
condition|)
block|{
name|metaData
operator|.
name|setUpgradeType
argument_list|(
name|UpgradeType
operator|.
name|UPGRADE_NOT_NEEDED
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|metaData
operator|.
name|setUpgradeType
argument_list|(
name|UpgradeType
operator|.
name|UPGRADE_NEEDED
argument_list|)
expr_stmt|;
block|}
return|return
name|metaData
return|;
block|}
specifier|protected
name|List
argument_list|<
name|UpgradeHandler
argument_list|>
name|getHandlersForVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|boolean
name|found
init|=
name|MIN_SUPPORTED_VERSION
operator|.
name|equals
argument_list|(
name|version
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|UpgradeHandler
argument_list|>
name|handlerList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|UpgradeHandler
argument_list|>
name|entry
range|:
name|handlers
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
continue|continue;
block|}
name|handlerList
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|handlerList
return|;
block|}
annotation|@
name|Override
specifier|public
name|Resource
name|upgradeProject
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|List
argument_list|<
name|UpgradeHandler
argument_list|>
name|handlerList
init|=
name|getHandlersForVersion
argument_list|(
name|loadProjectVersion
argument_list|(
name|resource
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|UpgradeUnit
argument_list|>
name|upgradeUnits
init|=
name|upgradeDOM
argument_list|(
name|resource
argument_list|,
name|handlerList
argument_list|)
decl_stmt|;
name|saveDOM
argument_list|(
name|upgradeUnits
argument_list|)
expr_stmt|;
name|resource
operator|=
name|upgradeUnits
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getResource
argument_list|()
expr_stmt|;
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|configurationTree
init|=
name|upgradeModel
argument_list|(
name|resource
argument_list|,
name|handlerList
argument_list|)
decl_stmt|;
name|saveModel
argument_list|(
name|configurationTree
argument_list|)
expr_stmt|;
return|return
name|resource
return|;
block|}
specifier|protected
name|List
argument_list|<
name|UpgradeUnit
argument_list|>
name|upgradeDOM
parameter_list|(
name|Resource
name|resource
parameter_list|,
name|List
argument_list|<
name|UpgradeHandler
argument_list|>
name|handlerList
parameter_list|)
block|{
name|List
argument_list|<
name|UpgradeUnit
argument_list|>
name|allUnits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Load DOM for all resources
name|Document
name|projectDocument
init|=
name|Util
operator|.
name|readDocument
argument_list|(
name|resource
operator|.
name|getURL
argument_list|()
argument_list|)
decl_stmt|;
name|UpgradeUnit
name|projectUnit
init|=
operator|new
name|UpgradeUnit
argument_list|(
name|resource
argument_list|,
name|projectDocument
argument_list|)
decl_stmt|;
name|allUnits
operator|.
name|add
argument_list|(
name|projectUnit
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Resource
argument_list|>
name|dataMapResources
init|=
name|getAdditionalDatamapResources
argument_list|(
name|projectUnit
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|UpgradeUnit
argument_list|>
name|dataMapUnits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|dataMapResources
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Resource
name|dataMapResource
range|:
name|dataMapResources
control|)
block|{
name|dataMapUnits
operator|.
name|add
argument_list|(
operator|new
name|UpgradeUnit
argument_list|(
name|dataMapResource
argument_list|,
name|Util
operator|.
name|readDocument
argument_list|(
name|dataMapResource
operator|.
name|getURL
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|allUnits
operator|.
name|addAll
argument_list|(
name|dataMapUnits
argument_list|)
expr_stmt|;
comment|// Update DOM
for|for
control|(
name|UpgradeHandler
name|handler
range|:
name|handlerList
control|)
block|{
name|handler
operator|.
name|processProjectDom
argument_list|(
name|projectUnit
argument_list|)
expr_stmt|;
for|for
control|(
name|UpgradeUnit
name|dataMapUnit
range|:
name|dataMapUnits
control|)
block|{
name|handler
operator|.
name|processDataMapDom
argument_list|(
name|dataMapUnit
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|allUnits
return|;
block|}
specifier|protected
name|void
name|saveDOM
parameter_list|(
name|Collection
argument_list|<
name|UpgradeUnit
argument_list|>
name|upgradeUnits
parameter_list|)
block|{
for|for
control|(
name|UpgradeUnit
name|unit
range|:
name|upgradeUnits
control|)
block|{
name|saveDocument
argument_list|(
name|unit
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|upgradeModel
parameter_list|(
name|Resource
name|resource
parameter_list|,
name|List
argument_list|<
name|UpgradeHandler
argument_list|>
name|handlerList
parameter_list|)
block|{
comment|// Load Model back from the update XML
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|configurationTree
init|=
name|loader
operator|.
name|load
argument_list|(
name|resource
argument_list|)
decl_stmt|;
comment|// Update model level if needed
for|for
control|(
name|UpgradeHandler
name|handler
range|:
name|handlerList
control|)
block|{
name|handler
operator|.
name|processModel
argument_list|(
name|configurationTree
operator|.
name|getRootNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|configurationTree
return|;
block|}
specifier|protected
name|void
name|saveModel
parameter_list|(
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|configurationTree
parameter_list|)
block|{
comment|// Save project once again via project saver, this will normalize XML to minimize final diff
name|Project
name|project
init|=
operator|new
name|Project
argument_list|(
name|configurationTree
argument_list|)
decl_stmt|;
name|projectSaver
operator|.
name|save
argument_list|(
name|project
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Resource
argument_list|>
name|getAdditionalDatamapResources
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
name|List
argument_list|<
name|Resource
argument_list|>
name|resources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|NodeList
name|nodes
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/domain/map/@name"
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
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|mapNode
init|=
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// in version 3.0.0.1 and earlier map tag had attribute location,
comment|// but it was always equal to data map name + ".map.xml"
name|Resource
name|mapResource
init|=
name|upgradeUnit
operator|.
name|getResource
argument_list|()
operator|.
name|getRelativeResource
argument_list|(
name|mapNode
operator|.
name|getNodeValue
argument_list|()
operator|+
literal|".map.xml"
argument_list|)
decl_stmt|;
name|resources
operator|.
name|add
argument_list|(
name|mapResource
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Can't get additional dataMap resources: "
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
name|resources
return|;
block|}
specifier|protected
name|void
name|saveDocument
parameter_list|(
name|UpgradeUnit
name|upgradeUnit
parameter_list|)
block|{
try|try
block|{
name|Source
name|input
init|=
operator|new
name|DOMSource
argument_list|(
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|Result
name|output
init|=
operator|new
name|StreamResult
argument_list|(
name|Util
operator|.
name|toFile
argument_list|(
name|upgradeUnit
operator|.
name|getResource
argument_list|()
operator|.
name|getURL
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|input
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Can't save the document: "
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A default method for quick extraction of the project version from an XML      * file.      */
specifier|protected
name|String
name|loadProjectVersion
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|RootTagHandler
name|rootHandler
init|=
operator|new
name|RootTagHandler
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|resource
operator|.
name|getURL
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|XMLReader
name|parser
init|=
name|Util
operator|.
name|createXmlReader
argument_list|()
decl_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setErrorHandler
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
comment|// expected... handler will terminate as soon as it finds a root tag.
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Error reading configuration from %s"
argument_list|,
name|e
argument_list|,
name|url
argument_list|)
throw|;
block|}
return|return
name|rootHandler
operator|.
name|projectVersion
operator|!=
literal|null
condition|?
name|rootHandler
operator|.
name|projectVersion
else|:
name|UNKNOWN_VERSION
return|;
block|}
specifier|protected
specifier|static
name|double
name|decodeVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|version
operator|==
literal|null
operator|||
name|isBlank
argument_list|(
name|version
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// leave the first dot, and treat remaining as a fraction
comment|// remove all non digit chars
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|version
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|dotProcessed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|version
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|nextChar
init|=
name|version
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextChar
operator|==
literal|'.'
operator|&&
operator|!
name|dotProcessed
condition|)
block|{
name|dotProcessed
operator|=
literal|true
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|nextChar
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|nextChar
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Double
operator|.
name|parseDouble
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|VersionComparator
implements|implements
name|Comparator
argument_list|<
name|String
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|VersionComparator
name|INSTANCE
init|=
operator|new
name|VersionComparator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|.
name|equals
argument_list|(
name|o2
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
name|double
name|v1Double
init|=
name|decodeVersion
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|double
name|v2Double
init|=
name|decodeVersion
argument_list|(
name|o2
argument_list|)
decl_stmt|;
return|return
name|v1Double
operator|<
name|v2Double
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
block|}
class|class
name|RootTagHandler
extends|extends
name|DefaultHandler
block|{
specifier|private
name|String
name|projectVersion
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|startElement
parameter_list|(
name|String
name|uri
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
throws|throws
name|SAXException
block|{
name|this
operator|.
name|projectVersion
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"project-version"
argument_list|)
expr_stmt|;
comment|// bail right away - we are not interested in reading this to the end
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"finished"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

