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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|crypto
operator|.
name|db
operator|.
name|Table1
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
name|crypto
operator|.
name|db
operator|.
name|Table4
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
name|crypto
operator|.
name|key
operator|.
name|KeySource
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
name|di
operator|.
name|Module
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
name|ObjectSelect
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|security
operator|.
name|Key
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_class
specifier|public
class|class
name|Runtime_LazyInit_IT
extends|extends
name|Runtime_AES128_Base
block|{
specifier|protected
specifier|static
name|boolean
name|UNLOCKED
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|setUp
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|UNLOCKED
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ServerRuntime
name|createRuntime
parameter_list|(
specifier|final
name|Module
name|crypto
parameter_list|)
block|{
name|Module
name|cryptoWrapper
init|=
name|binder
lambda|->
block|{
name|crypto
operator|.
name|configure
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|KeySource
operator|.
name|class
argument_list|)
operator|.
name|after
argument_list|(
name|LockingKeySourceDecorator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
decl_stmt|;
return|return
name|super
operator|.
name|createRuntime
argument_list|(
name|cryptoWrapper
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCryptoLocked
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|UNLOCKED
argument_list|)
expr_stmt|;
name|Table4
name|t4
init|=
name|runtime
operator|.
name|newContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|Table4
operator|.
name|class
argument_list|)
decl_stmt|;
name|t4
operator|.
name|setPlainInt
argument_list|(
literal|56
argument_list|)
expr_stmt|;
name|t4
operator|.
name|setPlainString
argument_list|(
literal|"XX"
argument_list|)
expr_stmt|;
name|t4
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|t4
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Table4
operator|.
name|class
argument_list|)
operator|.
name|selectOne
argument_list|(
name|runtime
operator|.
name|newContext
argument_list|()
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCryptoLocked_Unlocked
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|UNLOCKED
argument_list|)
expr_stmt|;
try|try
block|{
name|Table1
name|t1
init|=
name|runtime
operator|.
name|newContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
decl_stmt|;
name|t1
operator|.
name|setPlainInt
argument_list|(
literal|56
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoInt
argument_list|(
literal|77
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainString
argument_list|(
literal|"XX"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"YY"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Must have thrown on crypto access"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|UNLOCKED
operator|=
literal|true
expr_stmt|;
name|Table1
name|t1
init|=
name|runtime
operator|.
name|newContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
decl_stmt|;
name|t1
operator|.
name|setPlainInt
argument_list|(
literal|56
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoInt
argument_list|(
literal|77
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainString
argument_list|(
literal|"XX"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"YY"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|t1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
operator|.
name|selectOne
argument_list|(
name|runtime
operator|.
name|newContext
argument_list|()
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|LockingKeySourceDecorator
implements|implements
name|KeySource
block|{
specifier|private
name|KeySource
name|keySource
decl_stmt|;
specifier|public
name|LockingKeySourceDecorator
parameter_list|(
annotation|@
name|Inject
name|KeySource
name|keySource
parameter_list|)
block|{
name|this
operator|.
name|keySource
operator|=
name|keySource
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Key
name|getKey
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
return|return
name|ensureKeySource
argument_list|()
operator|.
name|getKey
argument_list|(
name|alias
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDefaultKeyAlias
parameter_list|()
block|{
return|return
name|ensureKeySource
argument_list|()
operator|.
name|getDefaultKeyAlias
argument_list|()
return|;
block|}
specifier|private
name|KeySource
name|ensureKeySource
parameter_list|()
block|{
if|if
condition|(
operator|!
name|UNLOCKED
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Crypto is locked"
argument_list|)
throw|;
block|}
return|return
name|keySource
return|;
block|}
block|}
block|}
end_class

end_unit

