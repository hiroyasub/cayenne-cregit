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
name|dba
operator|.
name|mysql
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
name|trans
operator|.
name|SelectTranslator
import|;
end_import

begin_comment
comment|/**  * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|MySQLSelectTranslator
extends|extends
name|SelectTranslator
block|{
specifier|public
name|String
name|createSqlString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
name|super
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// limit results
name|int
name|limit
init|=
name|getQuery
argument_list|()
operator|.
name|getMetaData
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getFetchLimit
argument_list|()
decl_stmt|;
if|if
condition|(
name|limit
operator|>
literal|0
condition|)
block|{
return|return
name|sql
operator|+
literal|" LIMIT "
operator|+
name|limit
return|;
block|}
return|return
name|sql
return|;
block|}
block|}
end_class

end_unit

