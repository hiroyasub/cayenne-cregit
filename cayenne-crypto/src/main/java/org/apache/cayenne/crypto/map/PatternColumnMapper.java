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
name|crypto
operator|.
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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

begin_comment
comment|/**  * A {@link ColumnMapper} that decides on whether a column is encrypted by  * matching its name against a preset pattern. Only column name is inspected.  * Table name is ignored.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|PatternColumnMapper
implements|implements
name|ColumnMapper
block|{
specifier|private
name|Pattern
name|columnNamePattern
decl_stmt|;
specifier|public
name|PatternColumnMapper
parameter_list|(
name|String
name|columnNamePattern
parameter_list|)
block|{
name|this
operator|.
name|columnNamePattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|columnNamePattern
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEncrypted
parameter_list|(
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
name|columnNamePattern
operator|.
name|matcher
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|find
argument_list|()
return|;
block|}
block|}
end_class

end_unit

