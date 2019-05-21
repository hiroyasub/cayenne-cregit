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
name|access
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
name|query
operator|.
name|ObjectSelect
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
name|compound
operator|.
name|CompoundFkTestEntity
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
name|compound
operator|.
name|CompoundOrder
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
name|compound
operator|.
name|CompoundOrderLine
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
name|compound
operator|.
name|CompoundOrderLineInfo
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
name|compound
operator|.
name|CompoundPkTestEntity
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
name|CayenneProjects
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
name|ServerCase
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
name|UseServerRuntime
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
name|util
operator|.
name|List
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

begin_comment
comment|/**  * Testing relationships with compound keys.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|COMPOUND_PROJECT
argument_list|)
specifier|public
class|class
name|DataContextCompoundRelIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context1
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testInsert
parameter_list|()
block|{
name|CompoundPkTestEntity
name|master
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompoundFkTestEntity
name|detail
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|master
operator|.
name|addToCompoundFkArray
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|master
operator|.
name|setName
argument_list|(
literal|"m1"
argument_list|)
expr_stmt|;
name|master
operator|.
name|setKey1
argument_list|(
literal|"key11"
argument_list|)
expr_stmt|;
name|master
operator|.
name|setKey2
argument_list|(
literal|"key21"
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setName
argument_list|(
literal|"d1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|master
argument_list|,
name|detail
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|CompoundPkTestEntity
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CompoundPkTestEntity
argument_list|>
name|objs
init|=
name|q
operator|.
name|select
argument_list|(
name|context1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"m1"
argument_list|,
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|CompoundFkTestEntity
argument_list|>
name|details
init|=
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCompoundFkArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|details
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d1"
argument_list|,
name|details
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFetchQualifyingToOne
parameter_list|()
block|{
name|CompoundPkTestEntity
name|master
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompoundPkTestEntity
name|master1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompoundFkTestEntity
name|detail
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompoundFkTestEntity
name|detail1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|master
operator|.
name|addToCompoundFkArray
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|master1
operator|.
name|addToCompoundFkArray
argument_list|(
name|detail1
argument_list|)
expr_stmt|;
name|master
operator|.
name|setName
argument_list|(
literal|"m1"
argument_list|)
expr_stmt|;
name|master
operator|.
name|setKey1
argument_list|(
literal|"key11"
argument_list|)
expr_stmt|;
name|master
operator|.
name|setKey2
argument_list|(
literal|"key21"
argument_list|)
expr_stmt|;
name|master1
operator|.
name|setName
argument_list|(
literal|"m2"
argument_list|)
expr_stmt|;
name|master1
operator|.
name|setKey1
argument_list|(
literal|"key12"
argument_list|)
expr_stmt|;
name|master1
operator|.
name|setKey2
argument_list|(
literal|"key22"
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setName
argument_list|(
literal|"d1"
argument_list|)
expr_stmt|;
name|detail1
operator|.
name|setName
argument_list|(
literal|"d2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|master
argument_list|,
name|master1
argument_list|,
name|detail
argument_list|,
name|detail1
argument_list|)
expr_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"toCompoundPk"
argument_list|,
name|master
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|CompoundFkTestEntity
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CompoundFkTestEntity
argument_list|>
name|objs
init|=
name|q
operator|.
name|select
argument_list|(
name|context1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d1"
argument_list|,
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToOne
parameter_list|()
block|{
block|{
name|CompoundOrder
name|order
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundOrder
operator|.
name|class
argument_list|)
decl_stmt|;
name|order
operator|.
name|setInfo
argument_list|(
literal|"order"
argument_list|)
expr_stmt|;
name|CompoundOrderLine
name|line
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundOrderLine
operator|.
name|class
argument_list|)
decl_stmt|;
name|line
operator|.
name|setOrder
argument_list|(
name|order
argument_list|)
expr_stmt|;
name|line
operator|.
name|setLineNumber
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|CompoundOrderLineInfo
name|info
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundOrderLineInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|info
operator|.
name|setLine
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|info
operator|.
name|setInfo
argument_list|(
literal|"info"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|order
argument_list|,
name|line
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
block|{
name|CompoundOrder
name|order
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|CompoundOrder
operator|.
name|class
argument_list|)
operator|.
name|selectFirst
argument_list|(
name|context1
argument_list|)
decl_stmt|;
name|CompoundOrderLine
name|line
init|=
name|order
operator|.
name|getLines
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|CompoundOrderLineInfo
name|info
init|=
name|line
operator|.
name|getInfo
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|line
operator|.
name|getLineNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"info"
argument_list|,
name|info
operator|.
name|getInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFetchQualifyingToMany
parameter_list|()
block|{
name|CompoundPkTestEntity
name|master
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompoundPkTestEntity
name|master1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompoundFkTestEntity
name|detail
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompoundFkTestEntity
name|detail1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|CompoundFkTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|master
operator|.
name|addToCompoundFkArray
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|master1
operator|.
name|addToCompoundFkArray
argument_list|(
name|detail1
argument_list|)
expr_stmt|;
name|master
operator|.
name|setName
argument_list|(
literal|"m1"
argument_list|)
expr_stmt|;
name|master
operator|.
name|setKey1
argument_list|(
literal|"key11"
argument_list|)
expr_stmt|;
name|master
operator|.
name|setKey2
argument_list|(
literal|"key21"
argument_list|)
expr_stmt|;
name|master1
operator|.
name|setName
argument_list|(
literal|"m2"
argument_list|)
expr_stmt|;
name|master1
operator|.
name|setKey1
argument_list|(
literal|"key12"
argument_list|)
expr_stmt|;
name|master1
operator|.
name|setKey2
argument_list|(
literal|"key22"
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setName
argument_list|(
literal|"d1"
argument_list|)
expr_stmt|;
name|detail1
operator|.
name|setName
argument_list|(
literal|"d2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|master
argument_list|,
name|master1
argument_list|,
name|detail
argument_list|,
name|detail1
argument_list|)
expr_stmt|;
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"compoundFkArray"
argument_list|,
name|detail1
argument_list|)
decl_stmt|;
name|SelectQuery
argument_list|<
name|CompoundPkTestEntity
argument_list|>
name|q
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|CompoundPkTestEntity
operator|.
name|class
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CompoundPkTestEntity
argument_list|>
name|objs
init|=
name|q
operator|.
name|select
argument_list|(
name|context1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"m2"
argument_list|,
name|objs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

