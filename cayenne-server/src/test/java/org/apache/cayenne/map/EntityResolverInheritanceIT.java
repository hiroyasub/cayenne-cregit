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
name|map
package|;
end_package

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
name|assertNotNull
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
name|assertNull
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
name|assertSame
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|PeopleProjectCase
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

begin_class
specifier|public
class|class
name|EntityResolverInheritanceIT
extends|extends
name|PeopleProjectCase
block|{
annotation|@
name|Inject
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetAbstractPersonTree
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityInheritanceTree
name|tree
init|=
name|resolver
operator|.
name|getInheritanceTree
argument_list|(
literal|"AbstractPerson"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tree
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"AbstractPerson"
argument_list|)
argument_list|,
name|tree
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetEmployeeTree
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityInheritanceTree
name|tree
init|=
name|resolver
operator|.
name|getInheritanceTree
argument_list|(
literal|"Employee"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tree
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|resolver
operator|.
name|getObjEntity
argument_list|(
literal|"Employee"
argument_list|)
argument_list|,
name|tree
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetManagerTree
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityInheritanceTree
name|tree
init|=
name|resolver
operator|.
name|getInheritanceTree
argument_list|(
literal|"Manager"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|tree
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLookupTreeRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|super1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"super1"
argument_list|)
decl_stmt|;
name|ObjEntity
name|sub1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"sub1"
argument_list|)
decl_stmt|;
name|ObjEntity
name|sub2
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"sub2"
argument_list|)
decl_stmt|;
name|super1
operator|.
name|setClassName
argument_list|(
literal|"java.lang.Float"
argument_list|)
expr_stmt|;
name|sub1
operator|.
name|setSuperEntityName
argument_list|(
literal|"super1"
argument_list|)
expr_stmt|;
name|sub1
operator|.
name|setClassName
argument_list|(
literal|"java.lang.Object"
argument_list|)
expr_stmt|;
name|sub2
operator|.
name|setSuperEntityName
argument_list|(
literal|"super1"
argument_list|)
expr_stmt|;
name|sub2
operator|.
name|setClassName
argument_list|(
literal|"java.lang.Integer"
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|super1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|sub1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|sub2
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|getInheritanceTree
argument_list|(
literal|"super1"
argument_list|)
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|addDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|EntityInheritanceTree
name|tree
init|=
name|resolver
operator|.
name|getInheritanceTree
argument_list|(
literal|"super1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|tree
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|super1
argument_list|,
name|tree
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

