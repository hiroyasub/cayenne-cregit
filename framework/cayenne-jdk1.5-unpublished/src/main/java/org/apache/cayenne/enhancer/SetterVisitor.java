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
name|enhancer
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
name|ObjectContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Label
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|MethodAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|MethodVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Opcodes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Type
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|SetterVisitor
extends|extends
name|MethodAdapter
block|{
specifier|private
name|EnhancementHelper
name|helper
decl_stmt|;
specifier|private
name|String
name|propertyName
decl_stmt|;
specifier|private
name|Type
name|propertyType
decl_stmt|;
specifier|public
name|SetterVisitor
parameter_list|(
name|MethodVisitor
name|mv
parameter_list|,
name|EnhancementHelper
name|helper
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Type
name|propertyType
parameter_list|)
block|{
name|super
argument_list|(
name|mv
argument_list|)
expr_stmt|;
name|this
operator|.
name|helper
operator|=
name|helper
expr_stmt|;
name|this
operator|.
name|propertyName
operator|=
name|propertyName
expr_stmt|;
name|this
operator|.
name|propertyType
operator|=
name|propertyType
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|visitCode
parameter_list|()
block|{
name|super
operator|.
name|visitCode
argument_list|()
expr_stmt|;
name|String
name|field
init|=
name|helper
operator|.
name|getPropertyField
argument_list|(
literal|"objectContext"
argument_list|)
decl_stmt|;
name|Type
name|objectContextType
init|=
name|Type
operator|.
name|getType
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|propertyDescriptor
init|=
name|propertyType
operator|.
name|getDescriptor
argument_list|()
decl_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ALOAD
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitFieldInsn
argument_list|(
name|Opcodes
operator|.
name|GETFIELD
argument_list|,
name|helper
operator|.
name|getCurrentClass
argument_list|()
operator|.
name|getInternalName
argument_list|()
argument_list|,
name|field
argument_list|,
name|objectContextType
operator|.
name|getDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|Label
name|l1
init|=
operator|new
name|Label
argument_list|()
decl_stmt|;
name|mv
operator|.
name|visitJumpInsn
argument_list|(
name|Opcodes
operator|.
name|IFNULL
argument_list|,
name|l1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ALOAD
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitFieldInsn
argument_list|(
name|Opcodes
operator|.
name|GETFIELD
argument_list|,
name|helper
operator|.
name|getCurrentClass
argument_list|()
operator|.
name|getInternalName
argument_list|()
argument_list|,
name|field
argument_list|,
name|objectContextType
operator|.
name|getDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ALOAD
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitLdcInsn
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ALOAD
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitFieldInsn
argument_list|(
name|Opcodes
operator|.
name|GETFIELD
argument_list|,
name|helper
operator|.
name|getCurrentClass
argument_list|()
operator|.
name|getInternalName
argument_list|()
argument_list|,
name|propertyName
argument_list|,
name|propertyDescriptor
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|visitPrimitiveConversion
argument_list|(
name|propertyDescriptor
argument_list|)
condition|)
block|{
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ALOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKEINTERFACE
argument_list|,
name|objectContextType
operator|.
name|getInternalName
argument_list|()
argument_list|,
literal|"propertyChanged"
argument_list|,
literal|"(Lorg/apache/cayenne/Persistent;Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitLabel
argument_list|(
name|l1
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|visitPrimitiveConversion
parameter_list|(
name|String
name|propertyDescriptor
parameter_list|)
block|{
if|if
condition|(
name|propertyDescriptor
operator|.
name|length
argument_list|()
operator|!=
literal|1
condition|)
block|{
return|return
literal|false
return|;
block|}
switch|switch
condition|(
name|propertyDescriptor
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
condition|)
block|{
case|case
literal|'I'
case|:
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Integer"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(I)Ljava/lang/Integer;"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ILOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Integer"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(I)Ljava/lang/Integer;"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
literal|'D'
case|:
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Double"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(D)Ljava/lang/Double;"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|DLOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Double"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(D)Ljava/lang/Double;"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
literal|'F'
case|:
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Float"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(F)Ljava/lang/Float;"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|FLOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Float"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(F)Ljava/lang/Float;"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
literal|'Z'
case|:
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Boolean"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(Z)Ljava/lang/Boolean;"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ILOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Boolean"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(Z)Ljava/lang/Boolean;"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
literal|'J'
case|:
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Long"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(J)Ljava/lang/Long;"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|LLOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Long"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(J)Ljava/lang/Long;"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
literal|'B'
case|:
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Byte"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(B)Ljava/lang/Byte;"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ILOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Byte"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(B)Ljava/lang/Byte;"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
literal|'C'
case|:
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Character"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(C)Ljava/lang/Character;"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ILOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Character"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(C)Ljava/lang/Character;"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
literal|'S'
case|:
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Short"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(S)Ljava/lang/Short;"
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitVarInsn
argument_list|(
name|Opcodes
operator|.
name|ILOAD
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMethodInsn
argument_list|(
name|Opcodes
operator|.
name|INVOKESTATIC
argument_list|,
literal|"java/lang/Short"
argument_list|,
literal|"valueOf"
argument_list|,
literal|"(S)Ljava/lang/Short;"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
default|default:
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

