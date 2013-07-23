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
name|unit
operator|.
name|jira
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|access
operator|.
name|DataDomain
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
name|server
operator|.
name|ServerModule
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
name|ObjEntity
import|;
end_import

begin_class
specifier|public
class|class
name|CAY_743Test
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testLoad2MapsWithCrossMapInheritance
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|ServerModule
argument_list|(
literal|"cay743/cayenne-domain.xml"
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|DataDomain
name|domain
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|DataDomain
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|domain
operator|.
name|getDataMaps
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|m1
init|=
name|domain
operator|.
name|getDataMap
argument_list|(
literal|"map1"
argument_list|)
decl_stmt|;
name|DataMap
name|m2
init|=
name|domain
operator|.
name|getDataMap
argument_list|(
literal|"map2"
argument_list|)
decl_stmt|;
name|ObjEntity
name|oe11
init|=
name|m1
operator|.
name|getObjEntity
argument_list|(
literal|"Entity11"
argument_list|)
decl_stmt|;
name|ObjEntity
name|oe12
init|=
name|m1
operator|.
name|getObjEntity
argument_list|(
literal|"Entity12"
argument_list|)
decl_stmt|;
name|ObjEntity
name|oe21
init|=
name|m2
operator|.
name|getObjEntity
argument_list|(
literal|"Entity21"
argument_list|)
decl_stmt|;
name|ObjEntity
name|oe22
init|=
name|m2
operator|.
name|getObjEntity
argument_list|(
literal|"Entity22"
argument_list|)
decl_stmt|;
comment|// this causes StackOverflow per CAY-743
name|ObjEntity
name|oe21Super
init|=
name|oe21
operator|.
name|getSuperEntity
argument_list|()
decl_stmt|;
name|ObjEntity
name|oe12Super
init|=
name|oe12
operator|.
name|getSuperEntity
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|oe12Super
argument_list|,
name|oe22
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|oe21Super
argument_list|,
name|oe11
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|injector
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
