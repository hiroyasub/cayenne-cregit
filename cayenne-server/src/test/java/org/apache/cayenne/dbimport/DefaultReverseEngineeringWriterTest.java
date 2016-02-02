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
name|dbimport
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
name|custommonkey
operator|.
name|xmlunit
operator|.
name|Diff
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
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|net
operator|.
name|URLDecoder
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

begin_class
specifier|public
class|class
name|DefaultReverseEngineeringWriterTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testWriteReverseEngineering
parameter_list|()
throws|throws
name|Exception
block|{
name|ReverseEngineeringWriter
name|engineering
init|=
operator|new
name|DefaultReverseEngineeringWriter
argument_list|()
decl_stmt|;
name|ReverseEngineering
name|reverseEngineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
name|Catalog
name|catalog1
init|=
operator|new
name|Catalog
argument_list|(
literal|"catalog1"
argument_list|)
decl_stmt|;
name|Catalog
name|catalog2
init|=
operator|new
name|Catalog
argument_list|(
literal|"catalog2"
argument_list|)
decl_stmt|;
name|Catalog
name|catalog3
init|=
operator|new
name|Catalog
argument_list|(
literal|"catalog3"
argument_list|)
decl_stmt|;
name|catalog1
operator|.
name|addSchema
argument_list|(
operator|new
name|Schema
argument_list|(
literal|"schema1"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addSchema
argument_list|(
operator|new
name|Schema
argument_list|(
literal|"schema2"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
literal|"excludedColumn1"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
literal|"excludedColumn2"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"includedColumn1"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"includedColumn2"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addExcludeProcedure
argument_list|(
operator|new
name|ExcludeProcedure
argument_list|(
literal|"excludedProcedure1"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addExcludeProcedure
argument_list|(
operator|new
name|ExcludeProcedure
argument_list|(
literal|"excludedProcedure2"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addIncludeProcedure
argument_list|(
operator|new
name|IncludeProcedure
argument_list|(
literal|"includedProcedure1"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addIncludeProcedure
argument_list|(
operator|new
name|IncludeProcedure
argument_list|(
literal|"includedProcedure2"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
literal|"excludedTable1"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
literal|"excludedTable2"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addIncludeTable
argument_list|(
operator|new
name|IncludeTable
argument_list|(
literal|"includedTable1"
argument_list|)
argument_list|)
expr_stmt|;
name|catalog1
operator|.
name|addIncludeTable
argument_list|(
operator|new
name|IncludeTable
argument_list|(
literal|"includedTable2"
argument_list|)
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|addCatalog
argument_list|(
name|catalog1
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|addCatalog
argument_list|(
name|catalog2
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|addCatalog
argument_list|(
name|catalog3
argument_list|)
expr_stmt|;
name|reverseEngineering
operator|.
name|addSchema
argument_list|(
operator|new
name|Schema
argument_list|(
literal|"schema3"
argument_list|)
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"reverseEngineering.xml"
argument_list|)
decl_stmt|;
name|String
name|decodedURL
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|Writer
name|printWriter
init|=
operator|new
name|PrintWriter
argument_list|(
name|decodedURL
argument_list|)
decl_stmt|;
name|reverseEngineering
operator|.
name|setConfigurationSource
argument_list|(
operator|new
name|URLResource
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
name|Resource
name|reverseEngineeringResource
init|=
name|engineering
operator|.
name|write
argument_list|(
name|reverseEngineering
argument_list|,
name|printWriter
argument_list|)
decl_stmt|;
name|assertReverseEngineering
argument_list|(
name|reverseEngineeringResource
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|assertReverseEngineering
parameter_list|(
name|Resource
name|resource
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|url1
init|=
name|resource
operator|.
name|getURL
argument_list|()
decl_stmt|;
name|URL
name|url2
init|=
name|getResource
argument_list|(
literal|"reverseEngineering-expected.xml"
argument_list|)
operator|.
name|getURL
argument_list|()
decl_stmt|;
name|FileReader
name|writedXML
decl_stmt|;
name|FileReader
name|expectedXML
decl_stmt|;
name|writedXML
operator|=
operator|new
name|FileReader
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|url1
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|expectedXML
operator|=
operator|new
name|FileReader
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|url2
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|Diff
name|diff
init|=
operator|new
name|Diff
argument_list|(
name|writedXML
argument_list|,
name|expectedXML
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|diff
operator|.
name|identical
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|URLResource
name|getResource
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|MalformedURLException
block|{
return|return
operator|new
name|URLResource
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

