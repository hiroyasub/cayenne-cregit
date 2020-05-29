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
operator|.
name|sqlbuilder
operator|.
name|sqltree
package|;
end_package

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_enum
specifier|public
enum|enum
name|NodeType
block|{
name|UNDEFINED
block|,
name|VALUE
block|,
name|COLUMN
block|,
name|LIMIT_OFFSET
block|,
name|FUNCTION
block|,
name|EQUALITY
block|,
name|LIKE
block|,
name|DISTINCT
block|,
name|IN
block|,
name|RESULT
block|,
name|WHERE
block|,
name|JOIN
block|,
name|FROM
block|,
name|UPDATE_SET
block|,
name|INSERT_COLUMNS
block|,
name|INSERT_VALUES
block|}
end_enum

end_unit

