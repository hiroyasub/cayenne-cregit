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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DbJoinTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|DbJoin
name|join
init|=
operator|new
name|DbJoin
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|join
operator|.
name|getRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|DbRelationship
name|relationship
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|join
operator|.
name|setRelationship
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|relationship
argument_list|,
name|join
operator|.
name|getRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|DbJoin
name|join
init|=
operator|new
name|DbJoin
argument_list|()
decl_stmt|;
name|join
operator|.
name|setSourceName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|join
operator|.
name|setTargetName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|String
name|string
init|=
name|join
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|string
argument_list|,
name|string
operator|.
name|startsWith
argument_list|(
literal|"org.apache.cayenne.map.DbJoin@"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|string
argument_list|,
name|string
operator|.
name|endsWith
argument_list|(
literal|"[source=X,target=Y]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

