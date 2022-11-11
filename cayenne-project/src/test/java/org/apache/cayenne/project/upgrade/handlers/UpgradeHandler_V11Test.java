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
name|configuration
operator|.
name|DataChannelDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|NamedNodeMap
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verifyNoInteractions
import|;
end_import

begin_comment
comment|/**  * @since 5.0  */
end_comment

begin_class
specifier|public
class|class
name|UpgradeHandler_V11Test
extends|extends
name|BaseUpgradeHandlerTest
block|{
name|UpgradeHandler
name|newHandler
parameter_list|()
block|{
return|return
operator|new
name|UpgradeHandler_V11
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProjectDomUpgrade
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|document
init|=
name|processProjectDom
argument_list|(
literal|"cayenne-project-v10.xml"
argument_list|)
decl_stmt|;
name|Element
name|root
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"11"
argument_list|,
name|root
operator|.
name|getAttribute
argument_list|(
literal|"project-version"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://cayenne.apache.org/schema/11/domain"
argument_list|,
name|root
operator|.
name|getAttribute
argument_list|(
literal|"xmlns"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|root
operator|.
name|getElementsByTagName
argument_list|(
literal|"map"
argument_list|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataMapDomUpgrade
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|document
init|=
name|processDataMapDom
argument_list|(
literal|"test-map-v10.map.xml"
argument_list|)
decl_stmt|;
name|Element
name|root
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"11"
argument_list|,
name|root
operator|.
name|getAttribute
argument_list|(
literal|"project-version"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://cayenne.apache.org/schema/11/modelMap"
argument_list|,
name|root
operator|.
name|getAttribute
argument_list|(
literal|"xmlns"
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|properties
init|=
name|root
operator|.
name|getElementsByTagName
argument_list|(
literal|"property"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|properties
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"defaultPackage"
argument_list|,
name|properties
operator|.
name|item
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"name"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|NodeList
name|objEntities
init|=
name|root
operator|.
name|getElementsByTagName
argument_list|(
literal|"obj-entity"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objEntities
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|objEntity
init|=
name|objEntities
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|NamedNodeMap
name|attributes
init|=
name|objEntity
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|attributes
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artist"
argument_list|,
name|attributes
operator|.
name|getNamedItem
argument_list|(
literal|"name"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artist"
argument_list|,
name|attributes
operator|.
name|getNamedItem
argument_list|(
literal|"dbEntityName"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|objEntity
operator|.
name|getChildNodes
argument_list|()
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://cayenne.apache.org/schema/11/info"
argument_list|,
name|objEntity
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getNextSibling
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"xmlns:info"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|root
operator|.
name|getElementsByTagName
argument_list|(
literal|"db-attribute"
argument_list|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCgenDomUpgrade
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|document
init|=
name|processDataMapDom
argument_list|(
literal|"test-map-v10.map.xml"
argument_list|)
decl_stmt|;
name|Element
name|root
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
comment|// check cgen config is updated
name|NodeList
name|cgens
init|=
name|root
operator|.
name|getElementsByTagName
argument_list|(
literal|"cgen"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cgens
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|cgenConfig
init|=
name|cgens
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://cayenne.apache.org/schema/11/cgen"
argument_list|,
name|cgenConfig
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItem
argument_list|(
literal|"xmlns"
argument_list|)
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|NodeList
name|childNodes
init|=
name|cgenConfig
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|boolean
name|dataMapTemplateSeen
init|=
literal|false
decl_stmt|;
name|boolean
name|dataMapSuperTemplateSeen
init|=
literal|false
decl_stmt|;
name|int
name|elements
init|=
literal|0
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
name|childNodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|childNodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|ELEMENT_NODE
condition|)
block|{
name|elements
operator|++
expr_stmt|;
switch|switch
condition|(
name|node
operator|.
name|getNodeName
argument_list|()
condition|)
block|{
case|case
literal|"client"
case|:
name|fail
argument_list|(
literal|"<client> tag is still present in the<cgen> config"
argument_list|)
expr_stmt|;
case|case
literal|"queryTemplate"
case|:
name|fail
argument_list|(
literal|"<queryTemplate> tag is still present in the<cgen> config"
argument_list|)
expr_stmt|;
case|case
literal|"querySuperTemplate"
case|:
name|fail
argument_list|(
literal|"<querySuperTemplate> tag is still present in the<cgen> config"
argument_list|)
expr_stmt|;
case|case
literal|"dataMapTemplate"
case|:
name|dataMapTemplateSeen
operator|=
literal|true
expr_stmt|;
break|break;
case|case
literal|"dataMapSuperTemplate"
case|:
name|dataMapSuperTemplateSeen
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|elements
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataMapTemplateSeen
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataMapSuperTemplateSeen
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testModelUpgrade
parameter_list|()
block|{
name|DataChannelDescriptor
name|descriptor
init|=
name|mock
argument_list|(
name|DataChannelDescriptor
operator|.
name|class
argument_list|)
decl_stmt|;
name|handler
operator|.
name|processModel
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|verifyNoInteractions
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

