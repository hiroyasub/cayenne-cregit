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
operator|.
name|handlers
package|;
end_package

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
name|UpgradeHandler_V9
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
name|UpgradeHandler_V9
operator|.
name|class
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
literal|"9"
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
name|Document
name|document
init|=
name|upgradeUnit
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|Element
name|dataMap
init|=
name|document
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
literal|"http://cayenne.apache.org/schema/9/modelMap"
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setAttribute
argument_list|(
literal|"xsi:schemaLocation"
argument_list|,
literal|"http://cayenne.apache.org/schema/9/modelMap "
operator|+
literal|"http://cayenne.apache.org/schema/9/modelMap.xsd"
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
try|try
block|{
name|Node
name|reNode
init|=
operator|(
name|Node
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/data-map/reverse-engineering-config"
argument_list|,
name|document
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|reNode
operator|!=
literal|null
condition|)
block|{
name|String
name|reFileName
init|=
operator|(
operator|(
name|Element
operator|)
name|reNode
operator|)
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
operator|+
literal|".xml"
decl_stmt|;
name|String
name|directoryPath
init|=
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
operator|.
name|getParent
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|directoryPath
operator|+
literal|"/"
operator|+
name|reFileName
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|dataMap
operator|.
name|removeChild
argument_list|(
name|reNode
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
literal|"Can't process dataMap DOM: "
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
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
block|}
block|}
end_class

end_unit

