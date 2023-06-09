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
name|gen
package|;
end_package

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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Set
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|DbEntity
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
name|ObjAttribute
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
name|SelectQueryDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|DataMapUtilsTest
block|{
specifier|protected
name|DataMapUtils
name|dataMapUtils
init|=
literal|null
decl_stmt|;
specifier|protected
name|ObjEntity
name|objEntity
init|=
literal|null
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|dataMapUtils
operator|=
operator|new
name|DataMapUtils
argument_list|()
expr_stmt|;
name|objEntity
operator|=
operator|new
name|ObjEntity
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|dataMapUtils
operator|=
literal|null
expr_stmt|;
name|objEntity
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetParameterNamesWithFilledQueriesMap
parameter_list|()
block|{
name|String
name|param
init|=
literal|"param"
decl_stmt|;
name|String
name|qualifierString
init|=
literal|"name = $"
operator|+
name|param
decl_stmt|;
name|SelectQueryDescriptor
name|selectQueryDescriptor
init|=
operator|new
name|SelectQueryDescriptor
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|result
argument_list|,
name|dataMapUtils
operator|.
name|getParameterNames
argument_list|(
name|selectQueryDescriptor
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|qualifierString
argument_list|)
decl_stmt|;
name|selectQueryDescriptor
operator|.
name|setQualifier
argument_list|(
name|exp
argument_list|)
expr_stmt|;
name|selectQueryDescriptor
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|param
argument_list|,
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|dataMapUtils
operator|.
name|queriesMap
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|collection
init|=
name|dataMapUtils
operator|.
name|getParameterNames
argument_list|(
name|selectQueryDescriptor
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|param
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|collection
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetParameterNamesWithEmptyQueriesMap
parameter_list|()
block|{
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|ObjAttribute
name|attribute
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|attribute
operator|.
name|setDbAttributePath
argument_list|(
literal|"testKey"
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setType
argument_list|(
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|String
name|param
init|=
literal|"param"
decl_stmt|;
name|String
name|qualifierString
init|=
literal|"name = $"
operator|+
name|param
decl_stmt|;
name|SelectQueryDescriptor
name|selectQueryDescriptor
init|=
operator|new
name|SelectQueryDescriptor
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|qualifierString
argument_list|)
decl_stmt|;
name|selectQueryDescriptor
operator|.
name|setQualifier
argument_list|(
name|exp
argument_list|)
expr_stmt|;
name|selectQueryDescriptor
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|selectQueryDescriptor
operator|.
name|setRoot
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|collection
init|=
name|dataMapUtils
operator|.
name|getParameterNames
argument_list|(
name|selectQueryDescriptor
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|queriesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|param
argument_list|,
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|queriesMap
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dataMapUtils
operator|.
name|queriesMap
argument_list|,
name|queriesMap
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|param
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|collection
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

