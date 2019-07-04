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
name|dbsync
operator|.
name|merge
operator|.
name|token
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|merge
operator|.
name|factory
operator|.
name|HSQLMergerTokenFactory
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
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|DbAttribute
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
name|DbJoin
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
name|DbRelationship
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
name|Procedure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|dbAttr
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|dbEntity
import|;
end_import

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|TokensReverseTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReverses
parameter_list|()
block|{
name|DbAttribute
name|attr
init|=
name|dbAttr
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|DbEntity
name|entity
init|=
name|dbEntity
argument_list|()
operator|.
name|attributes
argument_list|(
name|attr
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|DbRelationship
name|rel
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"rel"
argument_list|)
decl_stmt|;
name|rel
operator|.
name|setSourceEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|rel
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|rel
argument_list|,
name|attr
operator|.
name|getName
argument_list|()
argument_list|,
literal|"dontKnow"
argument_list|)
argument_list|)
expr_stmt|;
name|Procedure
name|procedure
init|=
operator|new
name|Procedure
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createAddColumnToDb
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createAddColumnToModel
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createDropColumnToDb
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createDropColumnToModel
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createAddRelationshipToDb
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createAddRelationshipToModel
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createDropRelationshipToDb
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createDropRelationshipToModel
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createCreateTableToDb
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createCreateTableToModel
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createDropTableToDb
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createDropTableToModel
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetAllowNullToDb
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetAllowNullToModel
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetNotNullToDb
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetNotNullToModel
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|DbAttribute
name|attr2
init|=
name|dbAttr
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetColumnTypeToDb
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|,
name|attr2
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetColumnTypeToModel
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|,
name|attr2
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetPrimaryKeyToDb
argument_list|(
name|entity
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|attr
argument_list|)
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|attr2
argument_list|)
argument_list|,
literal|"PK"
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetPrimaryKeyToModel
argument_list|(
name|entity
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|attr
argument_list|)
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|attr2
argument_list|)
argument_list|,
literal|"PK"
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createSetValueForNullToDb
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|,
operator|new
name|DefaultValueForNullProvider
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createDropProcedureToDb
argument_list|(
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createAddProcedureToDb
argument_list|(
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createDropProcedureToModel
argument_list|(
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
name|testOneToOneReverse
argument_list|(
name|factory
argument_list|()
operator|.
name|createAddProcedureToModel
argument_list|(
name|procedure
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testOneToOneReverse
parameter_list|(
name|MergerToken
name|token
parameter_list|)
block|{
name|MergerToken
name|token2
init|=
name|token
operator|.
name|createReverse
argument_list|(
name|factory
argument_list|()
argument_list|)
operator|.
name|createReverse
argument_list|(
name|factory
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|token
operator|.
name|getTokenName
argument_list|()
argument_list|,
name|token2
operator|.
name|getTokenName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|token
operator|.
name|getTokenValue
argument_list|()
argument_list|,
name|token2
operator|.
name|getTokenValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|token
operator|.
name|getDirection
argument_list|()
argument_list|,
name|token2
operator|.
name|getDirection
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|MergerTokenFactory
name|factory
parameter_list|()
block|{
return|return
operator|new
name|HSQLMergerTokenFactory
argument_list|()
return|;
block|}
block|}
end_class

end_unit

