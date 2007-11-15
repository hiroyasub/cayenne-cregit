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
name|tools
package|;
end_package

begin_comment
comment|/**  * Generic logging interface used by some of the tool classes.  *   * @author Kevin Menard  * @since 3.0  */
end_comment

begin_interface
interface|interface
name|ILog
block|{
comment|// These were taken from org.apache.tools.ant.Project and should be used for the msgLevel
comment|// parameter of the logging functions.
specifier|public
specifier|static
specifier|final
name|int
name|MSG_ERR
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|MSG_WARN
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|MSG_INFO
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|MSG_VERBOSE
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|MSG_DEBUG
init|=
literal|4
decl_stmt|;
name|void
name|log
parameter_list|(
name|String
name|msg
parameter_list|)
function_decl|;
name|void
name|log
parameter_list|(
name|String
name|msg
parameter_list|,
name|int
name|msgLevel
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

