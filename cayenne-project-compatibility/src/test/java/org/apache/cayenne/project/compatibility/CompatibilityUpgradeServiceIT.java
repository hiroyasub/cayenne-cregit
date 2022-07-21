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
name|compatibility
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|DIBootstrap
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
name|Injector
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
name|UpgradeService
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
name|resource
operator|.
name|URLResource
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CompatibilityUpgradeServiceIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|testUpgradeFullProjectDom
parameter_list|()
block|{
name|Injector
name|injector
init|=
name|getInjector
argument_list|()
decl_stmt|;
name|CompatibilityUpgradeService
name|upgradeService
init|=
operator|(
name|CompatibilityUpgradeService
operator|)
name|injector
operator|.
name|getInstance
argument_list|(
name|UpgradeService
operator|.
name|class
argument_list|)
decl_stmt|;
name|DocumentProvider
name|documentProvider
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DocumentProvider
operator|.
name|class
argument_list|)
decl_stmt|;
name|URL
name|resourceUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"cayenne-project-v6.xml"
argument_list|)
decl_stmt|;
name|Resource
name|resource
init|=
operator|new
name|URLResource
argument_list|(
name|resourceUrl
argument_list|)
decl_stmt|;
name|upgradeService
operator|.
name|upgradeProject
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|Document
name|domainDocument
init|=
name|documentProvider
operator|.
name|getDocument
argument_list|(
name|resourceUrl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|domainDocument
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"11"
argument_list|,
name|domainDocument
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"project-version"
argument_list|)
argument_list|)
expr_stmt|;
name|URL
name|dataMapUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-map-v6.map.xml"
argument_list|)
decl_stmt|;
name|Document
name|dataMapDocument
init|=
name|documentProvider
operator|.
name|getDocument
argument_list|(
name|dataMapUrl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dataMapDocument
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"11"
argument_list|,
name|dataMapDocument
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"project-version"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataMapDocument
operator|.
name|getElementsByTagName
argument_list|(
literal|"obj-entity"
argument_list|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataMapDocument
operator|.
name|getElementsByTagName
argument_list|(
literal|"db-entity"
argument_list|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataMapDocument
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
name|testUpgradeStandAloneDataMapDom
parameter_list|()
block|{
name|Injector
name|injector
init|=
name|getInjector
argument_list|()
decl_stmt|;
name|CompatibilityUpgradeService
name|upgradeService
init|=
operator|(
name|CompatibilityUpgradeService
operator|)
name|injector
operator|.
name|getInstance
argument_list|(
name|UpgradeService
operator|.
name|class
argument_list|)
decl_stmt|;
name|DocumentProvider
name|documentProvider
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DocumentProvider
operator|.
name|class
argument_list|)
decl_stmt|;
name|URL
name|dataMapUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test-map-v6.map.xml"
argument_list|)
decl_stmt|;
name|Document
name|dataMapDocument
init|=
name|documentProvider
operator|.
name|getDocument
argument_list|(
name|dataMapUrl
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|dataMapDocument
argument_list|)
expr_stmt|;
name|Resource
name|resource
init|=
operator|new
name|URLResource
argument_list|(
name|dataMapUrl
argument_list|)
decl_stmt|;
name|upgradeService
operator|.
name|upgradeDataMap
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|dataMapDocument
operator|=
name|documentProvider
operator|.
name|getDocument
argument_list|(
name|dataMapUrl
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|dataMapDocument
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"11"
argument_list|,
name|dataMapDocument
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"project-version"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|CompatibilityTestModule
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

