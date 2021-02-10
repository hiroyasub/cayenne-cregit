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
name|dba
operator|.
name|sqlserver
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|*
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
name|dba
operator|.
name|sqlserver
operator|.
name|sqltree
operator|.
name|SQLServerColumnNode
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
name|dba
operator|.
name|sqlserver
operator|.
name|sqltree
operator|.
name|SQLServerLimitOffsetNode
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
name|dba
operator|.
name|sybase
operator|.
name|SybaseSQLTreeProcessor
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerTreeProcessor
extends|extends
name|SybaseSQLTreeProcessor
block|{
specifier|private
name|Integer
name|version
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|onColumnNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|ColumnNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
operator|new
name|SQLServerColumnNode
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onLimitOffsetNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|LimitOffsetNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|version
operator|==
literal|null
operator|||
name|version
operator|>=
literal|12
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parent
operator|.
name|getChildrenCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|parent
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
operator|instanceof
name|OrderByNode
condition|)
block|{
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
operator|new
name|SQLServerLimitOffsetNode
argument_list|(
name|child
operator|.
name|getLimit
argument_list|()
argument_list|,
name|child
operator|.
name|getOffset
argument_list|()
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
name|super
operator|.
name|onLimitOffsetNode
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Integer
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|Integer
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
block|}
end_class

end_unit

