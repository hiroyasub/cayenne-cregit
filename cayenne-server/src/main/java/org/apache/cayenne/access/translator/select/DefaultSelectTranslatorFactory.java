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
name|translator
operator|.
name|select
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
name|CayenneRuntimeException
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
name|DbAdapter
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
name|EntityResolver
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
name|FluentSelect
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
name|Select
import|;
end_import

begin_comment
comment|/**  * A {@link SelectTranslator} factory that delegates translator creation to  * DbAdapter.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultSelectTranslatorFactory
implements|implements
name|SelectTranslatorFactory
block|{
annotation|@
name|Override
specifier|public
name|SelectTranslator
name|translator
parameter_list|(
name|Select
argument_list|<
name|?
argument_list|>
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
if|if
condition|(
name|query
operator|instanceof
name|FluentSelect
condition|)
block|{
return|return
name|adapter
operator|.
name|getSelectTranslator
argument_list|(
operator|(
name|FluentSelect
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|query
argument_list|,
name|entityResolver
argument_list|)
return|;
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported type of Select query %s"
argument_list|,
name|query
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

