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
name|lifecycle
operator|.
name|audit
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|EnumMap
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|Cayenne
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
name|ObjectContext
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
name|lifecycle
operator|.
name|changeset
operator|.
name|ChangeSetFilter
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
name|lifecycle
operator|.
name|db
operator|.
name|Auditable1
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
name|lifecycle
operator|.
name|db
operator|.
name|Auditable2
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
name|lifecycle
operator|.
name|db
operator|.
name|AuditableChild1
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
name|lifecycle
operator|.
name|db
operator|.
name|AuditableChild2
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
name|lifecycle
operator|.
name|db
operator|.
name|AuditableChild3
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
name|lifecycle
operator|.
name|db
operator|.
name|AuditableChildUuid
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
name|lifecycle
operator|.
name|id
operator|.
name|IdCoder
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
name|lifecycle
operator|.
name|relationship
operator|.
name|ObjectIdRelationshipHandler
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
name|lifecycle
operator|.
name|unit
operator|.
name|AuditableServerCase
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
name|AuditableFilterIT
extends|extends
name|AuditableServerCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAudit_IgnoreRuntimeRelationships
parameter_list|()
throws|throws
name|Exception
block|{
name|auditable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"xx"
argument_list|)
expr_stmt|;
name|auditable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"yy"
argument_list|)
expr_stmt|;
name|auditable1
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"aa"
argument_list|)
expr_stmt|;
name|auditableChild2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"zz"
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|Processor
argument_list|()
decl_stmt|;
name|AuditableFilter
name|filter
init|=
operator|new
name|AuditableFilter
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
comment|// prerequisite for BaseAuditableProcessor use
name|ChangeSetFilter
name|changeSetFilter
init|=
operator|new
name|ChangeSetFilter
argument_list|()
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|changeSetFilter
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Auditable1
name|a2
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Auditable1
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|AuditableChild2
name|a21
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|AuditableChild2
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|a21
operator|.
name|setParent
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|a21
operator|.
name|setCharProperty1
argument_list|(
literal|"XYZA"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|processor
operator|.
name|size
argument_list|)
expr_stmt|;
name|processor
operator|.
name|reset
argument_list|()
expr_stmt|;
name|Auditable1
name|a3
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Auditable1
operator|.
name|class
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|a21
operator|.
name|setParent
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|a3
operator|.
name|setCharProperty1
argument_list|(
literal|"12"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|processor
operator|.
name|size
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|audited
operator|.
name|get
argument_list|(
name|AuditableOperation
operator|.
name|UPDATE
argument_list|)
operator|.
name|contains
argument_list|(
name|a3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAudit_IncludeToManyRelationships
parameter_list|()
throws|throws
name|Exception
block|{
name|auditable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"xx"
argument_list|)
expr_stmt|;
name|auditable1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"yy"
argument_list|)
expr_stmt|;
name|auditableChild1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"zz"
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|Processor
argument_list|()
decl_stmt|;
name|AuditableFilter
name|filter
init|=
operator|new
name|AuditableFilter
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
comment|// prerequisite for BaseAuditableProcessor use
name|ChangeSetFilter
name|changeSetFilter
init|=
operator|new
name|ChangeSetFilter
argument_list|()
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|changeSetFilter
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Auditable1
name|a2
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Auditable1
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|AuditableChild1
name|a21
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|AuditableChild1
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|a21
operator|.
name|setParent
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|processor
operator|.
name|size
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|audited
operator|.
name|get
argument_list|(
name|AuditableOperation
operator|.
name|UPDATE
argument_list|)
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|audited
operator|.
name|get
argument_list|(
name|AuditableOperation
operator|.
name|UPDATE
argument_list|)
operator|.
name|contains
argument_list|(
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Auditable1
operator|.
name|class
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAudit_IgnoreProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|auditable2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"P1_1"
argument_list|,
literal|"P2_1"
argument_list|)
expr_stmt|;
name|auditable2
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"P1_2"
argument_list|,
literal|"P2_2"
argument_list|)
expr_stmt|;
name|auditable2
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"P1_3"
argument_list|,
literal|"P2_3"
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|Processor
argument_list|()
decl_stmt|;
name|AuditableFilter
name|filter
init|=
operator|new
name|AuditableFilter
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
comment|// prerequisite for BaseAuditableProcessor use
name|ChangeSetFilter
name|changeSetFilter
init|=
operator|new
name|ChangeSetFilter
argument_list|()
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|changeSetFilter
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Auditable2
name|a1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Auditable2
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Auditable2
name|a2
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Auditable2
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Auditable2
name|a3
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Auditable2
operator|.
name|class
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setCharProperty1
argument_list|(
literal|"__"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|setCharProperty2
argument_list|(
literal|"__"
argument_list|)
expr_stmt|;
name|a3
operator|.
name|setCharProperty1
argument_list|(
literal|"__"
argument_list|)
expr_stmt|;
name|a3
operator|.
name|setCharProperty2
argument_list|(
literal|"__"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|processor
operator|.
name|size
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|audited
operator|.
name|get
argument_list|(
name|AuditableOperation
operator|.
name|UPDATE
argument_list|)
operator|.
name|contains
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|audited
operator|.
name|get
argument_list|(
name|AuditableOperation
operator|.
name|UPDATE
argument_list|)
operator|.
name|contains
argument_list|(
name|a3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAuditableChild_IgnoreProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|auditable2
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"P1_1"
argument_list|,
literal|"P2_1"
argument_list|)
expr_stmt|;
name|auditable2
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"P1_2"
argument_list|,
literal|"P2_2"
argument_list|)
expr_stmt|;
name|auditableChild3
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"C"
argument_list|,
literal|"D"
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|Processor
argument_list|()
decl_stmt|;
name|AuditableFilter
name|filter
init|=
operator|new
name|AuditableFilter
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
comment|// prerequisite for BaseAuditableProcessor use
name|ChangeSetFilter
name|changeSetFilter
init|=
operator|new
name|ChangeSetFilter
argument_list|()
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|changeSetFilter
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|AuditableChild3
name|ac1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|AuditableChild3
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
comment|// a change to ignored property should not cause an audit event
name|ac1
operator|.
name|setCharProperty1
argument_list|(
literal|"X_X"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|processor
operator|.
name|size
argument_list|)
expr_stmt|;
name|processor
operator|.
name|reset
argument_list|()
expr_stmt|;
name|ac1
operator|.
name|setCharProperty2
argument_list|(
literal|"XXXXX"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|processor
operator|.
name|size
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAuditableChild_objectIdRelationship
parameter_list|()
throws|throws
name|Exception
block|{
name|auditable1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"xx"
argument_list|)
expr_stmt|;
name|auditableChildUuid
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"Auditable1:1"
argument_list|,
literal|"xxx"
argument_list|,
literal|"yyy"
argument_list|)
expr_stmt|;
name|DataDomain
name|domain
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|Processor
argument_list|()
decl_stmt|;
name|AuditableFilter
name|filter
init|=
operator|new
name|AuditableFilter
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
comment|// prerequisite for BaseAuditableProcessor use
name|ChangeSetFilter
name|changeSetFilter
init|=
operator|new
name|ChangeSetFilter
argument_list|()
decl_stmt|;
name|domain
operator|.
name|addFilter
argument_list|(
name|changeSetFilter
argument_list|)
expr_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|AuditableChildUuid
name|ac
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|AuditableChildUuid
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Auditable1
name|a1
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Auditable1
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|IdCoder
name|refHandler
init|=
operator|new
name|IdCoder
argument_list|(
name|domain
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectIdRelationshipHandler
name|handler
init|=
operator|new
name|ObjectIdRelationshipHandler
argument_list|(
name|refHandler
argument_list|)
decl_stmt|;
name|handler
operator|.
name|relate
argument_list|(
name|ac
argument_list|,
name|a1
argument_list|)
expr_stmt|;
name|ac
operator|.
name|setCharProperty1
argument_list|(
literal|"xxxx"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|processor
operator|.
name|size
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|auditables
init|=
name|processor
operator|.
name|audited
operator|.
name|get
argument_list|(
name|AuditableOperation
operator|.
name|UPDATE
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|auditables
operator|.
name|toArray
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|ac
operator|.
name|setCharProperty2
argument_list|(
literal|"yyyy"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|processor
operator|.
name|size
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|auditables
operator|.
name|toArray
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
class|class
name|Processor
implements|implements
name|AuditableProcessor
block|{
name|Map
argument_list|<
name|AuditableOperation
argument_list|,
name|Collection
argument_list|<
name|Object
argument_list|>
argument_list|>
name|audited
decl_stmt|;
name|int
name|size
decl_stmt|;
name|Processor
parameter_list|()
block|{
name|reset
argument_list|()
expr_stmt|;
block|}
name|void
name|reset
parameter_list|()
block|{
name|audited
operator|=
operator|new
name|EnumMap
argument_list|<>
argument_list|(
name|AuditableOperation
operator|.
name|class
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditableOperation
name|op
range|:
name|AuditableOperation
operator|.
name|values
argument_list|()
control|)
block|{
name|audited
operator|.
name|put
argument_list|(
name|op
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|audit
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|AuditableOperation
name|operation
parameter_list|)
block|{
name|audited
operator|.
name|get
argument_list|(
name|operation
argument_list|)
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|size
operator|++
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

