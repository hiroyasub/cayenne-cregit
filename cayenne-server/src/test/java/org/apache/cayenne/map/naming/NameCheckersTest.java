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
name|map
operator|.
name|naming
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
name|assertFalse
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
name|DataChannelDescriptor
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
name|DataNodeDescriptor
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
name|Embeddable
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
name|EmbeddableAttribute
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
name|ObjRelationship
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
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|ProcedureParameter
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
name|QueryDescriptor
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

begin_class
specifier|public
class|class
name|NameCheckersTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testObjEntityAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|NameCheckers
name|maker
init|=
name|NameCheckers
operator|.
name|objAttribute
decl_stmt|;
name|ObjEntity
name|namingContainer
init|=
operator|new
name|ObjEntity
argument_list|()
decl_stmt|;
name|String
name|baseName
init|=
name|maker
operator|.
name|baseName
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|baseName
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|name
operator|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseName
operator|+
literal|"1"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|name
operator|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseName
operator|+
literal|"2"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|name
operator|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseName
operator|+
literal|"3"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|maker
operator|=
name|NameCheckers
operator|.
name|objRelationship
expr_stmt|;
name|baseName
operator|=
name|maker
operator|.
name|baseName
argument_list|()
expr_stmt|;
name|name
operator|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseName
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|addRelationship
argument_list|(
operator|new
name|ObjRelationship
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|name
operator|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseName
operator|+
literal|"1"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|addRelationship
argument_list|(
operator|new
name|ObjRelationship
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|maker
operator|=
name|NameCheckers
operator|.
name|objCallbackMethod
expr_stmt|;
name|baseName
operator|=
name|maker
operator|.
name|baseName
argument_list|()
expr_stmt|;
name|name
operator|=
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|baseName
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|addRelationship
argument_list|(
operator|new
name|ObjRelationship
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEntity
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
operator|new
name|DbEntity
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|map
argument_list|,
name|NameCheckers
operator|.
name|dbEntity
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
operator|new
name|ObjEntity
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|map
argument_list|,
name|NameCheckers
operator|.
name|objEntity
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addProcedure
argument_list|(
operator|new
name|Procedure
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|map
argument_list|,
name|NameCheckers
operator|.
name|procedure
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|QueryDescriptor
name|query
init|=
name|QueryDescriptor
operator|.
name|selectQueryDescriptor
argument_list|()
decl_stmt|;
name|query
operator|.
name|setName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|map
operator|.
name|addQueryDescriptor
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|map
argument_list|,
name|NameCheckers
operator|.
name|query
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProject
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|NameCheckers
operator|.
name|dataChannelDescriptor
operator|.
name|isNameInUse
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDbEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|()
decl_stmt|;
name|dbEntity
operator|.
name|addRelationship
argument_list|(
operator|new
name|DbRelationship
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|dbEntity
argument_list|,
name|NameCheckers
operator|.
name|dbRelationship
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProcedureAttr
parameter_list|()
throws|throws
name|Exception
block|{
name|Procedure
name|procedure
init|=
operator|new
name|Procedure
argument_list|()
decl_stmt|;
name|procedure
operator|.
name|addCallParameter
argument_list|(
operator|new
name|ProcedureParameter
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|procedure
argument_list|,
name|NameCheckers
operator|.
name|procedureParameter
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmbeddableAttr
parameter_list|()
throws|throws
name|Exception
block|{
name|Embeddable
name|embeddable
init|=
operator|new
name|Embeddable
argument_list|()
decl_stmt|;
name|embeddable
operator|.
name|addAttribute
argument_list|(
operator|new
name|EmbeddableAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|embeddable
argument_list|,
name|NameCheckers
operator|.
name|embeddableAttribute
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDatanode
parameter_list|()
throws|throws
name|Exception
block|{
name|DataChannelDescriptor
name|descriptor
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|descriptor
argument_list|,
name|NameCheckers
operator|.
name|dataMap
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|getNodeDescriptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DataNodeDescriptor
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|descriptor
argument_list|,
name|NameCheckers
operator|.
name|dataNodeDescriptor
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDataMap
parameter_list|()
throws|throws
name|Exception
block|{
name|DataDomain
name|dataDomain
init|=
operator|new
name|DataDomain
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|dataDomain
operator|.
name|addDataMap
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|checkNameAndOther
argument_list|(
name|dataDomain
argument_list|,
name|NameCheckers
operator|.
name|dataMap
argument_list|,
literal|"name"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|NameCheckers
operator|.
name|dataMap
operator|.
name|isNameInUse
argument_list|(
literal|null
argument_list|,
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|NameCheckers
operator|.
name|dataMap
operator|.
name|isNameInUse
argument_list|(
literal|1
argument_list|,
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkNameAndOther
parameter_list|(
name|Object
name|namingContainer
parameter_list|,
name|NameCheckers
name|maker
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|maker
operator|.
name|isNameInUse
argument_list|(
name|namingContainer
argument_list|,
name|newName
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newName
operator|+
literal|"1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|,
name|newName
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"other"
operator|+
name|newName
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|maker
argument_list|,
name|namingContainer
argument_list|,
literal|"other"
operator|+
name|newName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOverlappingAttributeAndCallbackNames
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|namingContainer
init|=
operator|new
name|ObjEntity
argument_list|()
decl_stmt|;
name|namingContainer
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
literal|"myName"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"getMyName1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objCallbackMethod
argument_list|,
name|namingContainer
argument_list|,
literal|"getMyName"
argument_list|)
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPostAdd
argument_list|()
operator|.
name|addCallbackMethod
argument_list|(
literal|"getSecondName"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"SecondName1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objAttribute
argument_list|,
name|namingContainer
argument_list|,
literal|"SecondName"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"secondName1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objAttribute
argument_list|,
name|namingContainer
argument_list|,
literal|"secondName"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"SecondName1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objRelationship
argument_list|,
name|namingContainer
argument_list|,
literal|"SecondName"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"secondName1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objRelationship
argument_list|,
name|namingContainer
argument_list|,
literal|"secondName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAttributeDifferentInFirstLetterCases
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjEntity
name|namingContainer
init|=
operator|new
name|ObjEntity
argument_list|()
decl_stmt|;
name|namingContainer
operator|.
name|addAttribute
argument_list|(
operator|new
name|ObjAttribute
argument_list|(
literal|"myName"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|NameCheckers
operator|.
name|objAttribute
operator|.
name|isNameInUse
argument_list|(
name|namingContainer
argument_list|,
literal|"myName"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|NameCheckers
operator|.
name|objAttribute
operator|.
name|isNameInUse
argument_list|(
name|namingContainer
argument_list|,
literal|"MyName"
argument_list|)
argument_list|)
expr_stmt|;
name|namingContainer
operator|.
name|getCallbackMap
argument_list|()
operator|.
name|getPostAdd
argument_list|()
operator|.
name|addCallbackMethod
argument_list|(
literal|"getSecondName"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"SecondName1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objAttribute
argument_list|,
name|namingContainer
argument_list|,
literal|"SecondName"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"secondName1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|objAttribute
argument_list|,
name|namingContainer
argument_list|,
literal|"secondName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmbeddable
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|addEmbeddable
argument_list|(
operator|new
name|Embeddable
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|NameCheckers
operator|.
name|embeddable
operator|.
name|isNameInUse
argument_list|(
name|map
argument_list|,
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"name1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|embeddable
argument_list|,
name|map
argument_list|,
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|NameCheckers
operator|.
name|embeddable
operator|.
name|isNameInUse
argument_list|(
name|map
argument_list|,
literal|"other-name"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|setDefaultPackage
argument_list|(
literal|"package"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|NameCheckers
operator|.
name|embeddable
operator|.
name|isNameInUse
argument_list|(
name|map
argument_list|,
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"package.name"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|embeddable
argument_list|,
name|map
argument_list|,
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|addEmbeddable
argument_list|(
operator|new
name|Embeddable
argument_list|(
literal|"package.name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|NameCheckers
operator|.
name|embeddable
operator|.
name|isNameInUse
argument_list|(
name|map
argument_list|,
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"package.name1"
argument_list|,
name|DefaultUniqueNameGenerator
operator|.
name|generate
argument_list|(
name|NameCheckers
operator|.
name|embeddable
argument_list|,
name|map
argument_list|,
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|NameCheckers
operator|.
name|embeddable
operator|.
name|isNameInUse
argument_list|(
name|map
argument_list|,
literal|"other-name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

