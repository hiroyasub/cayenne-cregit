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
name|query
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
name|ObjectId
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
name|Persistent
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
name|types
operator|.
name|ValueObjectType
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
name|types
operator|.
name|ValueObjectTypeRegistry
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
name|exp
operator|.
name|TraversalHandler
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
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|eq
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
name|when
import|;
end_import

begin_comment
comment|/**  * This class is testing converting Expressions to cache key part.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SelectQueryMetadataCacheKeyTest
block|{
specifier|private
name|ValueObjectTypeRegistry
name|registry
decl_stmt|;
specifier|private
name|StringBuilder
name|cacheKey
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Before
specifier|public
name|void
name|createObjects
parameter_list|()
block|{
name|registry
operator|=
name|mock
argument_list|(
name|ValueObjectTypeRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// mock value type for Double class
name|ValueObjectType
name|mockType
init|=
name|mock
argument_list|(
name|ValueObjectType
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mockType
operator|.
name|getValueType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Double
operator|.
name|class
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockType
operator|.
name|toCacheKey
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"<value placeholder>"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|registry
operator|.
name|getValueType
argument_list|(
name|eq
argument_list|(
name|Double
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockType
argument_list|)
expr_stmt|;
comment|// value type for TestValue class
name|ValueObjectType
name|testType
init|=
operator|new
name|TestValueType
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|registry
operator|.
name|getValueType
argument_list|(
name|eq
argument_list|(
name|TestValue
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Simple expressions      */
annotation|@
name|Test
specifier|public
name|void
name|cacheKeySimple
parameter_list|()
block|{
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"field = 1"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"field = 1"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s2
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"field = 2"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s3
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|s2
argument_list|,
name|s3
argument_list|)
expr_stmt|;
block|}
comment|/**      * Expressions with list of simple values      */
annotation|@
name|Test
specifier|public
name|void
name|cacheKeyWithList
parameter_list|()
block|{
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"field in (1,2,3)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"field in (1,2,3)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s2
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"field in (2,3,4)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s3
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|s2
argument_list|,
name|s3
argument_list|)
expr_stmt|;
block|}
comment|/**      * Simple test for custom value object, Double.class is marked as a custom value object.      */
annotation|@
name|Test
specifier|public
name|void
name|cacheKeyWithValueObjectSimple
parameter_list|()
block|{
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"field = 1.0"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|s1
operator|.
name|contains
argument_list|(
literal|"<value placeholder>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * List of value objects, Double.class is marked as a custom value object.      */
annotation|@
name|Test
specifier|public
name|void
name|cacheKeyWithValueObjectList
parameter_list|()
block|{
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"field in (1.0,2.0,3.0)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|s1
operator|.
name|contains
argument_list|(
literal|"<value placeholder>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|cacheKeyWithEnumValue
parameter_list|()
block|{
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
name|TestEnum
operator|.
name|VALUE_1
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
name|TestEnum
operator|.
name|VALUE_1
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s2
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
name|TestEnum
operator|.
name|VALUE_2
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s3
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|s2
argument_list|,
name|s3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|cacheKeyWithValueObject
parameter_list|()
block|{
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
operator|new
name|TestValue
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
operator|new
name|TestValue
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s2
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
operator|new
name|TestValue
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s3
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|s2
argument_list|,
name|s3
argument_list|)
expr_stmt|;
block|}
comment|/**      * Persistent objects should be converted to their ObjectIds.      */
annotation|@
name|Test
specifier|public
name|void
name|cacheKeyWithPersistentObject
parameter_list|()
block|{
name|Persistent
name|persistent1
init|=
name|mock
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectId
name|objectId1
init|=
name|mock
argument_list|(
name|ObjectId
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|objectId1
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"objId1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|persistent1
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|objectId1
argument_list|)
expr_stmt|;
name|Persistent
name|persistent2
init|=
name|mock
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectId
name|objectId2
init|=
name|mock
argument_list|(
name|ObjectId
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|objectId2
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"objId2"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|persistent2
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|objectId2
argument_list|)
expr_stmt|;
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
name|persistent1
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
name|persistent1
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s2
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
literal|"testPath"
argument_list|,
name|persistent2
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s3
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|s1
operator|.
name|contains
argument_list|(
literal|"objId1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s3
operator|.
name|contains
argument_list|(
literal|"objId2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|s2
argument_list|,
name|s3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|cacheKeyWithFunctionCall
parameter_list|()
block|{
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"length(testPath)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s1
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"length(testPath)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s2
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"count(testPath)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s3
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|s2
argument_list|,
name|s3
argument_list|)
expr_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"substring(path, testPath)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s4
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"substring(path2, testPath)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s5
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertNotEquals
argument_list|(
name|s4
argument_list|,
name|s5
argument_list|)
expr_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"year(path)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s6
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"hour(path)"
argument_list|)
operator|.
name|traverse
argument_list|(
name|newHandler
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s7
init|=
name|cacheKey
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertNotEquals
argument_list|(
name|s6
argument_list|,
name|s7
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TraversalHandler
name|newHandler
parameter_list|()
block|{
return|return
operator|new
name|SelectQueryMetadata
operator|.
name|ToCacheKeyTraversalHandler
argument_list|(
name|registry
argument_list|,
name|cacheKey
operator|=
operator|new
name|StringBuilder
argument_list|()
argument_list|)
return|;
block|}
comment|/* ************* Test types *************** */
comment|/**      * Test enum      */
enum|enum
name|TestEnum
block|{
name|VALUE_1
block|,
name|VALUE_2
block|}
comment|/**      * Test value object      */
specifier|static
class|class
name|TestValue
block|{
name|int
name|v
init|=
literal|0
decl_stmt|;
name|TestValue
parameter_list|(
name|int
name|v
parameter_list|)
block|{
name|this
operator|.
name|v
operator|=
name|v
expr_stmt|;
block|}
block|}
comment|/**      * Test value object descriptor, we need only toCacheKey() method      */
specifier|static
class|class
name|TestValueType
implements|implements
name|ValueObjectType
argument_list|<
name|TestValue
argument_list|,
name|Integer
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|Integer
argument_list|>
name|getTargetType
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|TestValue
argument_list|>
name|getValueType
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|TestValue
name|toJavaObject
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|fromJavaObject
parameter_list|(
name|TestValue
name|object
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toCacheKey
parameter_list|(
name|TestValue
name|object
parameter_list|)
block|{
return|return
name|Integer
operator|.
name|toString
argument_list|(
name|object
operator|.
name|v
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

