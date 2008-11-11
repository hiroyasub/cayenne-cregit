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
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|List
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ConfigLoaderTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testCase1
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version='1.0' encoding='utf-8'?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domains>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domain name='domain1'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domain>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domains>"
argument_list|)
expr_stmt|;
name|ConfigLoaderCase
name|aCase
init|=
operator|new
name|ConfigLoaderCase
argument_list|()
decl_stmt|;
name|aCase
operator|.
name|setConfigInfo
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setTotalDomains
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|runCase
argument_list|(
name|aCase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCase2
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version='1.0' encoding='utf-8'?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domains>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domain name='domain1'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<map name='m1' location='aaa'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domain>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domains>"
argument_list|)
expr_stmt|;
name|ConfigLoaderCase
name|aCase
init|=
operator|new
name|ConfigLoaderCase
argument_list|()
decl_stmt|;
name|aCase
operator|.
name|setConfigInfo
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setTotalDomains
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setFailedMaps
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|runCase
argument_list|(
name|aCase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCase3
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version='1.0' encoding='utf-8'?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domains>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domain name='domain1'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<map name='m1' location='testmap.map.xml'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domain>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domains>"
argument_list|)
expr_stmt|;
name|ConfigLoaderCase
name|aCase
init|=
operator|new
name|ConfigLoaderCase
argument_list|()
decl_stmt|;
name|aCase
operator|.
name|setConfigInfo
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setTotalDomains
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|runCase
argument_list|(
name|aCase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCase4
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version='1.0' encoding='utf-8'?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domains>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domain name='domain1'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<map name='m1' location='testmap.map.xml'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<node name='db1' datasource='node.xml'"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n              factory='org.apache.cayenne.conf.DriverDataSourceFactory'"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n              adapter='org.apache.cayenne.dba.mysql.MySQLAdapter'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</node>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domain>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domains>"
argument_list|)
expr_stmt|;
name|ConfigLoaderCase
name|aCase
init|=
operator|new
name|ConfigLoaderCase
argument_list|()
decl_stmt|;
name|aCase
operator|.
name|setConfigInfo
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setFailedDataSources
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setTotalDomains
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|runCase
argument_list|(
name|aCase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCase5
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version='1.0' encoding='utf-8'?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domains>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domain name='domain1'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<map name='m1' location='testmap.map.xml'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<node name='db1' datasource='node.xml'"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n              factory='org.apache.cayenne.conf.DriverDataSourceFactory'"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n              adapter='org.apache.cayenne.dba.mysql.MySQLAdapter'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<map-ref name='m1'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</node>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domain>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domains>"
argument_list|)
expr_stmt|;
name|ConfigLoaderCase
name|aCase
init|=
operator|new
name|ConfigLoaderCase
argument_list|()
decl_stmt|;
name|aCase
operator|.
name|setConfigInfo
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setFailedDataSources
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setTotalDomains
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|runCase
argument_list|(
name|aCase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCase6
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version='1.0' encoding='utf-8'?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domains>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domain name='domain1'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<map name='m1' location='testmap.map.xml'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domain>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<view name='v1' location='testview1.view.xml'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<view name='v2' location='testview2.view.xml'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domains>"
argument_list|)
expr_stmt|;
name|ConfigLoaderCase
name|aCase
init|=
operator|new
name|ConfigLoaderCase
argument_list|()
decl_stmt|;
name|aCase
operator|.
name|setConfigInfo
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|aCase
operator|.
name|setTotalDomains
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|runCase
argument_list|(
name|aCase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCase7
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"<?xml version='1.0' encoding='utf-8'?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domains>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domain name='d1'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<node name='n1' datasource='node.xml'"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\nfactory='org.apache.cayenne.conf.MockDataSourceFactory'"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\nadapter='org.apache.cayenne.dba.mysql.MySQLAdapter'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domain>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<domain name='d2'>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n<node name='n2' datasource='node.xml'"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\nfactory='org.apache.cayenne.conf.MockDataSourceFactory1'"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\nadapter='org.apache.cayenne.dba.mysql.MySQLAdapter'/>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domain>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n</domains>"
argument_list|)
expr_stmt|;
specifier|final
name|List
name|nodes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|MockConfigLoaderDelegate
name|delegate
init|=
operator|new
name|MockConfigLoaderDelegate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|shouldLoadDataNode
parameter_list|(
name|String
name|domainName
parameter_list|,
name|String
name|nodeName
parameter_list|,
name|String
name|dataSource
parameter_list|,
name|String
name|adapter
parameter_list|,
name|String
name|factory
parameter_list|)
block|{
name|NodeLoadState
name|state
init|=
operator|new
name|NodeLoadState
argument_list|()
decl_stmt|;
name|state
operator|.
name|domainName
operator|=
name|domainName
expr_stmt|;
name|state
operator|.
name|nodeName
operator|=
name|nodeName
expr_stmt|;
name|state
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|nodes
operator|.
name|add
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|ConfigLoader
name|helper
init|=
operator|new
name|ConfigLoader
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|helper
operator|.
name|loadDomains
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|nodes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|NodeLoadState
name|s1
init|=
operator|(
name|NodeLoadState
operator|)
name|nodes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"d1"
argument_list|,
name|s1
operator|.
name|domainName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"n1"
argument_list|,
name|s1
operator|.
name|nodeName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cayenne.conf.MockDataSourceFactory"
argument_list|,
name|s1
operator|.
name|factory
argument_list|)
expr_stmt|;
name|NodeLoadState
name|s2
init|=
operator|(
name|NodeLoadState
operator|)
name|nodes
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"d2"
argument_list|,
name|s2
operator|.
name|domainName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"n2"
argument_list|,
name|s2
operator|.
name|nodeName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cayenne.conf.MockDataSourceFactory1"
argument_list|,
name|s2
operator|.
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runCase
parameter_list|(
name|ConfigLoaderCase
name|aCase
parameter_list|)
throws|throws
name|Exception
block|{
name|Configuration
name|conf
init|=
operator|new
name|EmptyConfiguration
argument_list|()
decl_stmt|;
name|ConfigLoaderDelegate
name|delegate
init|=
name|conf
operator|.
name|getLoaderDelegate
argument_list|()
decl_stmt|;
name|ConfigLoader
name|helper
init|=
operator|new
name|ConfigLoader
argument_list|(
name|delegate
argument_list|)
decl_stmt|;
name|aCase
operator|.
name|test
argument_list|(
name|helper
argument_list|)
expr_stmt|;
block|}
class|class
name|NodeLoadState
block|{
name|String
name|domainName
decl_stmt|;
name|String
name|nodeName
decl_stmt|;
name|String
name|factory
decl_stmt|;
block|}
block|}
end_class

end_unit

