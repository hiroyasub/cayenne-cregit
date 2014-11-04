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
name|merge
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
name|DataObject
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
name|DataContext
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
name|jdbc
operator|.
name|ParameterBinding
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
name|sql
operator|.
name|Types
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|ValueForNullIT
extends|extends
name|MergeCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_VALUE_STRING
init|=
literal|"DEFSTRING"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|ObjEntity
name|objEntity
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
comment|// insert some rows before adding "not null" column
specifier|final
name|int
name|nrows
init|=
literal|10
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nrows
condition|;
name|i
operator|++
control|)
block|{
name|DataObject
name|o
init|=
operator|(
name|DataObject
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|o
operator|.
name|writeProperty
argument_list|(
literal|"paintingTitle"
argument_list|,
literal|"ptitle"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// create and add new column to model and db
name|DbAttribute
name|column
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NEWCOL2"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
name|column
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|column
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dbEntity
operator|.
name|getAttributes
argument_list|()
operator|.
name|contains
argument_list|(
name|column
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|column
argument_list|,
name|dbEntity
operator|.
name|getAttribute
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// need obj attr to be able to query
name|ObjAttribute
name|objAttr
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"newcol2"
argument_list|)
decl_stmt|;
name|objAttr
operator|.
name|setDbAttributePath
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|addAttribute
argument_list|(
name|objAttr
argument_list|)
expr_stmt|;
comment|// check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// set not null
name|column
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// merge to db
name|assertTokensAndExecute
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// check values for null
name|Expression
name|qual
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|objAttr
operator|.
name|getName
argument_list|()
argument_list|,
name|DEFAULT_VALUE_STRING
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
literal|"Painting"
argument_list|,
name|qual
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Persistent
argument_list|>
name|rows
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
name|nrows
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// clean up
name|dbEntity
operator|.
name|removeAttribute
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|DbMerger
name|createMerger
parameter_list|(
name|MergerFactory
name|mergerFactory
parameter_list|,
specifier|final
name|ValueForNullProvider
name|valueForNullProvider
parameter_list|)
block|{
return|return
name|super
operator|.
name|createMerger
argument_list|(
name|mergerFactory
argument_list|,
operator|new
name|DefaultValueForNullProvider
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|ParameterBinding
name|get
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|int
name|type
init|=
name|column
operator|.
name|getType
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|VARCHAR
case|:
return|return
operator|new
name|ParameterBinding
argument_list|(
name|DEFAULT_VALUE_STRING
argument_list|,
name|type
argument_list|,
operator|-
literal|1
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
literal|"should not get here"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasValueFor
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

