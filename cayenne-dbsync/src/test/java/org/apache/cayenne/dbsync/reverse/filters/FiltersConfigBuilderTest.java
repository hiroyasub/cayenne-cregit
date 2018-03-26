begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|reverse
operator|.
name|filters
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Catalog
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeColumn
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeProcedure
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ExcludeTable
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeColumn
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeProcedure
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeTable
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|ReverseEngineering
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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
operator|.
name|Schema
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

begin_class
specifier|public
class|class
name|FiltersConfigBuilderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCompact_01
parameter_list|()
block|{
name|ReverseEngineering
name|engineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
name|engineering
operator|.
name|addIncludeTable
argument_list|(
operator|new
name|IncludeTable
argument_list|(
literal|"table1"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addIncludeTable
argument_list|(
operator|new
name|IncludeTable
argument_list|(
literal|"table2"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addIncludeTable
argument_list|(
operator|new
name|IncludeTable
argument_list|(
literal|"table3"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"includeColumn"
argument_list|)
argument_list|)
expr_stmt|;
name|FiltersConfigBuilder
name|builder
init|=
operator|new
name|FiltersConfigBuilder
argument_list|(
name|engineering
argument_list|)
decl_stmt|;
name|builder
operator|.
name|compact
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ReverseEngineering: \n"
operator|+
literal|"  Catalog: null\n"
operator|+
literal|"    Schema: null\n"
operator|+
literal|"      IncludeTable: table1\n"
operator|+
literal|"        IncludeColumn: includeColumn\n"
operator|+
literal|"      IncludeTable: table2\n"
operator|+
literal|"        IncludeColumn: includeColumn\n"
operator|+
literal|"      IncludeTable: table3\n"
operator|+
literal|"        IncludeColumn: includeColumn\n\n"
operator|+
literal|"  Use primitives"
argument_list|,
name|engineering
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompact_02
parameter_list|()
block|{
name|ReverseEngineering
name|engineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
name|engineering
operator|.
name|addCatalog
argument_list|(
operator|new
name|Catalog
argument_list|(
literal|"catalogName"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addSchema
argument_list|(
operator|new
name|Schema
argument_list|(
literal|"schemaName01"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addSchema
argument_list|(
operator|new
name|Schema
argument_list|(
literal|"schemaName02"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addIncludeTable
argument_list|(
operator|new
name|IncludeTable
argument_list|(
literal|"table1"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
literal|"table2"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"includeColumn"
argument_list|)
argument_list|)
expr_stmt|;
name|FiltersConfigBuilder
name|builder
init|=
operator|new
name|FiltersConfigBuilder
argument_list|(
name|engineering
argument_list|)
decl_stmt|;
name|builder
operator|.
name|compact
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ReverseEngineering: \n"
operator|+
literal|"  Catalog: catalogName\n"
operator|+
literal|"    Schema: schemaName01\n"
operator|+
literal|"      IncludeTable: table1\n"
operator|+
literal|"        IncludeColumn: includeColumn\n"
operator|+
literal|"      ExcludeTable: table2\n"
operator|+
literal|"    Schema: schemaName02\n"
operator|+
literal|"      IncludeTable: table1\n"
operator|+
literal|"        IncludeColumn: includeColumn\n"
operator|+
literal|"      ExcludeTable: table2\n\n"
operator|+
literal|"  Use primitives"
argument_list|,
name|engineering
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompact_03
parameter_list|()
block|{
name|ReverseEngineering
name|engineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
name|engineering
operator|.
name|addCatalog
argument_list|(
operator|new
name|Catalog
argument_list|(
literal|"APP1"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addCatalog
argument_list|(
operator|new
name|Catalog
argument_list|(
literal|"APP2"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
literal|"SYS_.*"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
literal|"calculated_.*"
argument_list|)
argument_list|)
expr_stmt|;
name|FiltersConfigBuilder
name|builder
init|=
operator|new
name|FiltersConfigBuilder
argument_list|(
name|engineering
argument_list|)
decl_stmt|;
name|builder
operator|.
name|compact
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ReverseEngineering: \n"
operator|+
literal|"  Catalog: APP1\n"
operator|+
literal|"    Schema: null\n"
operator|+
literal|"      IncludeTable: null\n"
operator|+
literal|"        ExcludeColumn: calculated_.*\n"
operator|+
literal|"      ExcludeTable: SYS_.*\n"
operator|+
literal|"  Catalog: APP2\n"
operator|+
literal|"    Schema: null\n"
operator|+
literal|"      IncludeTable: null\n"
operator|+
literal|"        ExcludeColumn: calculated_.*\n"
operator|+
literal|"      ExcludeTable: SYS_.*\n\n"
operator|+
literal|"  Use primitives"
argument_list|,
name|engineering
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompact_04
parameter_list|()
block|{
name|ReverseEngineering
name|engineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
name|engineering
operator|.
name|addSchema
argument_list|(
operator|new
name|Schema
argument_list|(
literal|"s"
argument_list|)
argument_list|)
expr_stmt|;
name|FiltersConfigBuilder
name|builder
init|=
operator|new
name|FiltersConfigBuilder
argument_list|(
name|engineering
argument_list|)
decl_stmt|;
name|builder
operator|.
name|compact
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ReverseEngineering: \n"
operator|+
literal|"  Catalog: null\n"
operator|+
literal|"    Schema: s\n"
operator|+
literal|"      IncludeTable: null\n\n"
operator|+
literal|"  Use primitives"
argument_list|,
name|engineering
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompact_full
parameter_list|()
block|{
name|ReverseEngineering
name|engineering
init|=
operator|new
name|ReverseEngineering
argument_list|()
decl_stmt|;
name|Catalog
name|cat01
init|=
operator|new
name|Catalog
argument_list|(
literal|"cat_01"
argument_list|)
decl_stmt|;
name|Schema
name|sch01
init|=
operator|new
name|Schema
argument_list|(
literal|"sch_01"
argument_list|)
decl_stmt|;
name|sch01
operator|.
name|addIncludeTable
argument_list|(
name|includeTable
argument_list|(
literal|"t1"
argument_list|,
literal|"c11"
argument_list|,
literal|"c12"
argument_list|)
argument_list|)
expr_stmt|;
name|sch01
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
literal|"t2"
argument_list|)
argument_list|)
expr_stmt|;
name|sch01
operator|.
name|addIncludeProcedure
argument_list|(
operator|new
name|IncludeProcedure
argument_list|(
literal|"p1"
argument_list|)
argument_list|)
expr_stmt|;
name|sch01
operator|.
name|addExcludeProcedure
argument_list|(
operator|new
name|ExcludeProcedure
argument_list|(
literal|"p2"
argument_list|)
argument_list|)
expr_stmt|;
name|sch01
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"c_x1"
argument_list|)
argument_list|)
expr_stmt|;
name|sch01
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
literal|"c_x2"
argument_list|)
argument_list|)
expr_stmt|;
name|cat01
operator|.
name|addSchema
argument_list|(
name|sch01
argument_list|)
expr_stmt|;
name|cat01
operator|.
name|addIncludeTable
argument_list|(
name|includeTable
argument_list|(
literal|"t3"
argument_list|,
literal|"c31"
argument_list|,
literal|"c32"
argument_list|)
argument_list|)
expr_stmt|;
name|cat01
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
literal|"t4"
argument_list|)
argument_list|)
expr_stmt|;
name|cat01
operator|.
name|addIncludeProcedure
argument_list|(
operator|new
name|IncludeProcedure
argument_list|(
literal|"p3"
argument_list|)
argument_list|)
expr_stmt|;
name|cat01
operator|.
name|addExcludeProcedure
argument_list|(
operator|new
name|ExcludeProcedure
argument_list|(
literal|"p4"
argument_list|)
argument_list|)
expr_stmt|;
name|cat01
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"c_xx1"
argument_list|)
argument_list|)
expr_stmt|;
name|cat01
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
literal|"c_xx2"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addCatalog
argument_list|(
name|cat01
argument_list|)
expr_stmt|;
name|Schema
name|sch02
init|=
operator|new
name|Schema
argument_list|(
literal|"sch_02"
argument_list|)
decl_stmt|;
name|sch02
operator|.
name|addIncludeTable
argument_list|(
name|includeTable
argument_list|(
literal|"t5"
argument_list|,
literal|"c51"
argument_list|,
literal|"c52"
argument_list|)
argument_list|)
expr_stmt|;
name|sch02
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
literal|"t6"
argument_list|)
argument_list|)
expr_stmt|;
name|sch02
operator|.
name|addIncludeProcedure
argument_list|(
operator|new
name|IncludeProcedure
argument_list|(
literal|"p5"
argument_list|)
argument_list|)
expr_stmt|;
name|sch02
operator|.
name|addExcludeProcedure
argument_list|(
operator|new
name|ExcludeProcedure
argument_list|(
literal|"p6"
argument_list|)
argument_list|)
expr_stmt|;
name|sch02
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"c2_x1"
argument_list|)
argument_list|)
expr_stmt|;
name|sch02
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
literal|"c2_x2"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addSchema
argument_list|(
name|sch02
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addIncludeTable
argument_list|(
name|includeTable
argument_list|(
literal|"t7"
argument_list|,
literal|"c71"
argument_list|,
literal|"c72"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addExcludeTable
argument_list|(
operator|new
name|ExcludeTable
argument_list|(
literal|"t8"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addIncludeProcedure
argument_list|(
operator|new
name|IncludeProcedure
argument_list|(
literal|"p7"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addExcludeProcedure
argument_list|(
operator|new
name|ExcludeProcedure
argument_list|(
literal|"p8"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"c_xxx1"
argument_list|)
argument_list|)
expr_stmt|;
name|engineering
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
literal|"c_xxx2"
argument_list|)
argument_list|)
expr_stmt|;
name|FiltersConfigBuilder
name|builder
init|=
operator|new
name|FiltersConfigBuilder
argument_list|(
name|engineering
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Original ReverseEngineering should be"
argument_list|,
literal|"ReverseEngineering: \n"
operator|+
literal|"  Catalog: cat_01\n"
operator|+
literal|"    Schema: sch_01\n"
operator|+
literal|"      IncludeTable: t1\n"
operator|+
literal|"        IncludeColumn: c11\n"
operator|+
literal|"        ExcludeColumn: c12\n"
operator|+
literal|"      ExcludeTable: t2\n"
operator|+
literal|"      IncludeColumn: c_x1\n"
operator|+
literal|"      ExcludeColumn: c_x2\n"
operator|+
literal|"      IncludeProcedure: p1\n"
operator|+
literal|"      ExcludeProcedure: p2\n"
operator|+
literal|"      IncludeTable: t3\n"
operator|+
literal|"        IncludeColumn: c31\n"
operator|+
literal|"        ExcludeColumn: c32\n"
operator|+
literal|"      ExcludeTable: t4\n"
operator|+
literal|"      IncludeColumn: c_xx1\n"
operator|+
literal|"      ExcludeColumn: c_xx2\n"
operator|+
literal|"      IncludeProcedure: p3\n"
operator|+
literal|"      ExcludeProcedure: p4\n"
operator|+
literal|"  Schema: sch_02\n"
operator|+
literal|"    IncludeTable: t5\n"
operator|+
literal|"      IncludeColumn: c51\n"
operator|+
literal|"      ExcludeColumn: c52\n"
operator|+
literal|"    ExcludeTable: t6\n"
operator|+
literal|"    IncludeColumn: c2_x1\n"
operator|+
literal|"    ExcludeColumn: c2_x2\n"
operator|+
literal|"    IncludeProcedure: p5\n"
operator|+
literal|"    ExcludeProcedure: p6\n"
operator|+
literal|"    IncludeTable: t7\n"
operator|+
literal|"      IncludeColumn: c71\n"
operator|+
literal|"      ExcludeColumn: c72\n"
operator|+
literal|"    ExcludeTable: t8\n"
operator|+
literal|"    IncludeColumn: c_xxx1\n"
operator|+
literal|"    ExcludeColumn: c_xxx2\n"
operator|+
literal|"    IncludeProcedure: p7\n"
operator|+
literal|"    ExcludeProcedure: p8\n\n"
operator|+
literal|"  Use primitives"
argument_list|,
name|engineering
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|compact
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ReverseEngineering: \n"
operator|+
literal|"  Catalog: cat_01\n"
operator|+
literal|"    Schema: sch_01\n"
operator|+
literal|"      IncludeTable: t1\n"
operator|+
literal|"        IncludeColumn: c11\n"
operator|+
literal|"        IncludeColumn: c_xxx1\n"
operator|+
literal|"        IncludeColumn: c_xx1\n"
operator|+
literal|"        IncludeColumn: c_x1\n"
operator|+
literal|"        ExcludeColumn: c12\n"
operator|+
literal|"        ExcludeColumn: c_xxx2\n"
operator|+
literal|"        ExcludeColumn: c_xx2\n"
operator|+
literal|"        ExcludeColumn: c_x2\n"
operator|+
literal|"      IncludeTable: t7\n"
operator|+
literal|"        IncludeColumn: c71\n"
operator|+
literal|"        IncludeColumn: c_xxx1\n"
operator|+
literal|"        ExcludeColumn: c72\n"
operator|+
literal|"        ExcludeColumn: c_xxx2\n"
operator|+
literal|"      IncludeTable: t3\n"
operator|+
literal|"        IncludeColumn: c31\n"
operator|+
literal|"        IncludeColumn: c_xxx1\n"
operator|+
literal|"        IncludeColumn: c_xx1\n"
operator|+
literal|"        ExcludeColumn: c32\n"
operator|+
literal|"        ExcludeColumn: c_xxx2\n"
operator|+
literal|"        ExcludeColumn: c_xx2\n"
operator|+
literal|"      ExcludeTable: t2\n"
operator|+
literal|"      ExcludeTable: t8\n"
operator|+
literal|"      ExcludeTable: t4\n"
operator|+
literal|"      IncludeProcedure: p1\n"
operator|+
literal|"      IncludeProcedure: p7\n"
operator|+
literal|"      IncludeProcedure: p3\n"
operator|+
literal|"      ExcludeProcedure: p2\n"
operator|+
literal|"      ExcludeProcedure: p8\n"
operator|+
literal|"      ExcludeProcedure: p4\n"
operator|+
literal|"    Schema: sch_02\n"
operator|+
literal|"      IncludeTable: t5\n"
operator|+
literal|"        IncludeColumn: c51\n"
operator|+
literal|"        IncludeColumn: c_xxx1\n"
operator|+
literal|"        IncludeColumn: c2_x1\n"
operator|+
literal|"        ExcludeColumn: c52\n"
operator|+
literal|"        ExcludeColumn: c_xxx2\n"
operator|+
literal|"        ExcludeColumn: c2_x2\n"
operator|+
literal|"      IncludeTable: t7\n"
operator|+
literal|"        IncludeColumn: c71\n"
operator|+
literal|"        IncludeColumn: c_xxx1\n"
operator|+
literal|"        ExcludeColumn: c72\n"
operator|+
literal|"        ExcludeColumn: c_xxx2\n"
operator|+
literal|"      ExcludeTable: t6\n"
operator|+
literal|"      ExcludeTable: t8\n"
operator|+
literal|"      IncludeProcedure: p5\n"
operator|+
literal|"      IncludeProcedure: p7\n"
operator|+
literal|"      ExcludeProcedure: p6\n"
operator|+
literal|"      ExcludeProcedure: p8\n\n"
operator|+
literal|"  Use primitives"
argument_list|,
name|engineering
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|IncludeTable
name|includeTable
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|incCol
parameter_list|,
name|String
name|excCol
parameter_list|)
block|{
name|IncludeTable
name|incTable01
init|=
operator|new
name|IncludeTable
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|incTable01
operator|.
name|addIncludeColumn
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
name|incCol
argument_list|)
argument_list|)
expr_stmt|;
name|incTable01
operator|.
name|addExcludeColumn
argument_list|(
operator|new
name|ExcludeColumn
argument_list|(
name|excCol
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|incTable01
return|;
block|}
block|}
end_class

end_unit

