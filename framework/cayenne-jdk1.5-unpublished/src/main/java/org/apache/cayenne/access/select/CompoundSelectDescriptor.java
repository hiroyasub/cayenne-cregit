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
operator|.
name|select
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|CayenneException
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|CompoundSelectDescriptor
implements|implements
name|SelectDescriptor
argument_list|<
name|Object
index|[]
argument_list|>
block|{
specifier|private
name|SelectDescriptor
argument_list|<
name|Object
argument_list|>
index|[]
name|segments
decl_stmt|;
specifier|private
name|CompoundRowReader
name|rowReader
decl_stmt|;
specifier|private
name|List
argument_list|<
name|SelectColumn
argument_list|>
name|columns
decl_stmt|;
name|CompoundSelectDescriptor
parameter_list|(
name|SelectDescriptor
argument_list|<
name|Object
argument_list|>
index|[]
name|segments
parameter_list|)
block|{
name|this
operator|.
name|segments
operator|=
name|segments
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|SelectColumn
argument_list|>
name|getColumns
parameter_list|()
block|{
return|return
name|columns
return|;
block|}
specifier|public
name|RowReader
argument_list|<
name|Object
index|[]
argument_list|>
name|getRowReader
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
throws|throws
name|CayenneException
block|{
if|if
condition|(
name|rowReader
operator|==
literal|null
condition|)
block|{
name|willReadResultSet
argument_list|(
name|resultSet
argument_list|)
expr_stmt|;
block|}
return|return
name|rowReader
return|;
block|}
specifier|private
name|void
name|willReadResultSet
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
throws|throws
name|CayenneException
block|{
name|this
operator|.
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<
name|SelectColumn
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|rowReader
operator|=
operator|new
name|CompoundRowReader
argument_list|(
name|segments
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// finish descriptor initialization
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|segments
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|offset
init|=
name|columns
operator|.
name|size
argument_list|()
decl_stmt|;
name|columns
operator|.
name|addAll
argument_list|(
name|segments
index|[
name|i
index|]
operator|.
name|getColumns
argument_list|()
argument_list|)
expr_stmt|;
name|RowReader
argument_list|<
name|Object
argument_list|>
name|rowReader
init|=
name|segments
index|[
name|i
index|]
operator|.
name|getRowReader
argument_list|(
name|resultSet
argument_list|)
decl_stmt|;
name|rowReader
operator|.
name|setColumnOffset
argument_list|(
name|offset
argument_list|)
expr_stmt|;
name|this
operator|.
name|rowReader
operator|.
name|addRowReader
argument_list|(
name|i
argument_list|,
name|rowReader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

