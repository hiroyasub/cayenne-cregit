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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataObjectUtils
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
name|query
operator|.
name|SelectQuery
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
name|testdo
operator|.
name|inherit
operator|.
name|AbstractPerson
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
name|testdo
operator|.
name|inherit
operator|.
name|Address
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
name|testdo
operator|.
name|inherit
operator|.
name|ClientCompany
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
name|testdo
operator|.
name|inherit
operator|.
name|CustomerRepresentative
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
name|testdo
operator|.
name|inherit
operator|.
name|Department
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
name|testdo
operator|.
name|inherit
operator|.
name|Employee
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
name|testdo
operator|.
name|inherit
operator|.
name|Manager
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
name|testdo
operator|.
name|inherit
operator|.
name|RelatedEntity
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
name|testdo
operator|.
name|inherit
operator|.
name|BaseEntity
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
name|testdo
operator|.
name|inherit
operator|.
name|SubEntity
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
name|testdo
operator|.
name|inherit
operator|.
name|DirectToSubEntity
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
name|PeopleCase
import|;
end_import

begin_comment
comment|/**  * Testing Cayenne behavior with DataObject inheritance hierarchies.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|InheritanceTest
extends|extends
name|PeopleCase
block|{
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSave
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientCompany
name|company
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientCompany
operator|.
name|class
argument_list|)
decl_stmt|;
name|company
operator|.
name|setName
argument_list|(
literal|"Boeing"
argument_list|)
expr_stmt|;
name|CustomerRepresentative
name|rep
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CustomerRepresentative
operator|.
name|class
argument_list|)
decl_stmt|;
name|rep
operator|.
name|setName
argument_list|(
literal|"Joe Schmoe"
argument_list|)
expr_stmt|;
name|rep
operator|.
name|setToClientCompany
argument_list|(
name|company
argument_list|)
expr_stmt|;
name|rep
operator|.
name|setPersonType
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|Employee
name|employee
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Employee
operator|.
name|class
argument_list|)
decl_stmt|;
name|employee
operator|.
name|setName
argument_list|(
literal|"Our Joe Schmoe"
argument_list|)
expr_stmt|;
name|employee
operator|.
name|setPersonType
argument_list|(
literal|"E"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContextWithDedicatedCache
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|CustomerRepresentative
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
name|reps
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countObjectOfClass
argument_list|(
name|reps
argument_list|,
name|CustomerRepresentative
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that to-one relationship produces correct subclass.      */
specifier|public
name|void
name|testEmployeeAddress
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testEmployeeAddress"
argument_list|)
expr_stmt|;
name|List
name|addresses
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Address
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|addresses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Address
name|address
init|=
operator|(
name|Address
operator|)
name|addresses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|Employee
operator|.
name|class
argument_list|,
name|address
operator|.
name|getToEmployee
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that to-one relationship produces correct subclass.      */
specifier|public
name|void
name|testManagerAddress
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testManagerAddress"
argument_list|)
expr_stmt|;
name|List
name|addresses
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Address
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|addresses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Address
name|address
init|=
operator|(
name|Address
operator|)
name|addresses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Employee
name|e
init|=
name|address
operator|.
name|getToEmployee
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|Manager
operator|.
name|class
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCAY592
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testManagerAddress"
argument_list|)
expr_stmt|;
name|List
name|addresses
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Address
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|addresses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Address
name|address
init|=
operator|(
name|Address
operator|)
name|addresses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Employee
name|e
init|=
name|address
operator|.
name|getToEmployee
argument_list|()
decl_stmt|;
comment|// CAY-592 - make sure modification of the address in a parallel context
comment|// doesn't mess up the Manager
name|DataContext
name|c2
init|=
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|createDataContext
argument_list|()
decl_stmt|;
name|e
operator|=
operator|(
name|Employee
operator|)
name|DataObjectUtils
operator|.
name|objectForPK
argument_list|(
name|c2
argument_list|,
name|e
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|address
operator|=
operator|(
name|Address
operator|)
name|e
operator|.
name|getAddresses
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e
argument_list|,
name|address
operator|.
name|getToEmployee
argument_list|()
argument_list|)
expr_stmt|;
name|address
operator|.
name|setCity
argument_list|(
literal|"XYZ"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e
argument_list|,
name|address
operator|.
name|getToEmployee
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that to-one relationship produces correct subclass.      */
specifier|public
name|void
name|testRepCompany
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testRepCompany"
argument_list|)
expr_stmt|;
name|List
name|companies
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|ClientCompany
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|companies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ClientCompany
name|company
init|=
operator|(
name|ClientCompany
operator|)
name|companies
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
name|reps
init|=
name|company
operator|.
name|getRepresentatives
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|CustomerRepresentative
operator|.
name|class
argument_list|,
name|reps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests that to-many relationship produces correct subclasses.      */
specifier|public
name|void
name|testDepartmentEmployees
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testDepartmentEmployees"
argument_list|)
expr_stmt|;
name|List
name|departments
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Department
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|departments
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Department
name|dept
init|=
operator|(
name|Department
operator|)
name|departments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
name|employees
init|=
name|dept
operator|.
name|getEmployees
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|employees
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|countObjectOfClass
argument_list|(
name|employees
argument_list|,
name|Employee
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countObjectOfClass
argument_list|(
name|employees
argument_list|,
name|Manager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectNoInheritanceResolving
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testSelect"
argument_list|)
expr_stmt|;
comment|// select Abstract Ppl
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|AbstractPerson
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setResolvingInherited
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|query
operator|.
name|isResolvingInherited
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|abstractPpl
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|abstractPpl
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|countObjectOfClass
argument_list|(
name|abstractPpl
argument_list|,
name|CustomerRepresentative
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|countObjectOfClass
argument_list|(
name|abstractPpl
argument_list|,
name|Employee
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|countObjectOfClass
argument_list|(
name|abstractPpl
argument_list|,
name|Manager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSelectInheritanceResolving
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testSelect"
argument_list|)
expr_stmt|;
comment|// select Abstract Ppl
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|AbstractPerson
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setResolvingInherited
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|isResolvingInherited
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|abstractPpl
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|abstractPpl
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countObjectOfClass
argument_list|(
name|abstractPpl
argument_list|,
name|CustomerRepresentative
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|countObjectOfClass
argument_list|(
name|abstractPpl
argument_list|,
name|Employee
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|countObjectOfClass
argument_list|(
name|abstractPpl
argument_list|,
name|Manager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test for CAY-1008: Reverse relationships may not be correctly set if inheritance is used.      */
specifier|public
name|void
name|testCAY1008
parameter_list|()
block|{
name|RelatedEntity
name|related
init|=
name|context
operator|.
name|newObject
argument_list|(
name|RelatedEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|BaseEntity
name|base
init|=
name|context
operator|.
name|newObject
argument_list|(
name|BaseEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|base
operator|.
name|setToRelatedEntity
argument_list|(
name|related
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|related
operator|.
name|getBaseEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|related
operator|.
name|getSubEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SubEntity
name|sub
init|=
name|context
operator|.
name|newObject
argument_list|(
name|SubEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|sub
operator|.
name|setToRelatedEntity
argument_list|(
name|related
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|related
operator|.
name|getBaseEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|related
operator|.
name|getSubEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test for CAY-1009: Bogus runtime relationships can mess up commit.      */
specifier|public
name|void
name|testCAY1009
parameter_list|()
block|{
comment|// We should have only one relationship.  DirectToSubEntity -> SubEntity.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"DirectToSubEntity"
argument_list|)
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Simulate a load from a default configuration.
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|applyObjectLayerDefaults
argument_list|()
expr_stmt|;
comment|// We should still just have the one mapped relationship, but we in fact now have two:
comment|// DirectToSubEntity -> BaseEntity and DirectToSubEntity -> SubEntity.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
literal|"DirectToSubEntity"
argument_list|)
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DirectToSubEntity
name|direct
init|=
name|context
operator|.
name|newObject
argument_list|(
name|DirectToSubEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|SubEntity
name|sub
init|=
name|context
operator|.
name|newObject
argument_list|(
name|SubEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|sub
operator|.
name|setToDirectToSubEntity
argument_list|(
name|direct
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|direct
operator|.
name|getSubEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
name|sub
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|direct
operator|.
name|getSubEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a number of objects of a particular class and subclasses in the list.      */
specifier|protected
name|int
name|countObjectOfClass
parameter_list|(
name|List
name|objects
parameter_list|,
name|Class
name|aClass
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|objects
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|aClass
operator|.
name|isAssignableFrom
argument_list|(
name|next
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
block|}
block|}
return|return
name|i
return|;
block|}
block|}
end_class

end_unit

