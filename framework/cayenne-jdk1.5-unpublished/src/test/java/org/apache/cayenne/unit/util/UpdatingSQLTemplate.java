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
name|unit
operator|.
name|util
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
name|query
operator|.
name|SQLTemplate
import|;
end_import

begin_comment
comment|/**  * A SQLTemplate simplified for Spring configuration.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|UpdatingSQLTemplate
extends|extends
name|SQLTemplate
block|{
specifier|public
name|UpdatingSQLTemplate
parameter_list|(
name|Class
name|rootClass
parameter_list|,
name|String
name|defaultTemplate
parameter_list|)
block|{
name|super
argument_list|(
name|rootClass
argument_list|,
name|defaultTemplate
operator|!=
literal|null
condition|?
name|defaultTemplate
operator|.
name|trim
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

