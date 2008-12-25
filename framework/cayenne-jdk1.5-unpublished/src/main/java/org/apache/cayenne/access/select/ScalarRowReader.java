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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneException
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
name|ExtendedType
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|ScalarRowReader
implements|implements
name|RowReader
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
name|ExtendedType
name|converter
decl_stmt|;
specifier|private
name|int
name|index
decl_stmt|;
specifier|private
name|int
name|jdbcType
decl_stmt|;
name|ScalarRowReader
parameter_list|(
name|ExtendedType
name|converter
parameter_list|,
name|int
name|jdbcType
parameter_list|)
block|{
name|this
operator|.
name|converter
operator|=
name|converter
expr_stmt|;
name|this
operator|.
name|index
operator|=
literal|1
expr_stmt|;
name|this
operator|.
name|jdbcType
operator|=
name|jdbcType
expr_stmt|;
block|}
specifier|public
name|void
name|setColumnOffset
parameter_list|(
name|int
name|offset
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|offset
operator|+
literal|1
expr_stmt|;
block|}
specifier|public
name|Object
name|readRow
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
throws|throws
name|CayenneException
block|{
try|try
block|{
return|return
name|converter
operator|.
name|materializeObject
argument_list|(
name|resultSet
argument_list|,
name|index
argument_list|,
name|jdbcType
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|CayenneException
name|cex
parameter_list|)
block|{
comment|// rethrow unmodified
throw|throw
name|cex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|otherex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneException
argument_list|(
literal|"Exception materializing column."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|otherex
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

