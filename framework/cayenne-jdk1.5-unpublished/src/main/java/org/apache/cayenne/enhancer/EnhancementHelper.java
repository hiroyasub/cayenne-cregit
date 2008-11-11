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
name|objectweb
operator|.
name|asm
operator|.
name|ClassVisitor
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
name|FieldVisitor
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
comment|/**  * A helper for the ASM ClassVisitor that encapsulates common class enhancement  * operations.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|EnhancementHelper
block|{
comment|/**      * Returns whether the field name matches the naming pattern of fields generated by      * Cayenne enhancer.      */
specifier|public
specifier|static
name|boolean
name|isGeneratedField
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|!=
literal|null
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|fieldPrefix
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|fieldPrefix
init|=
literal|"$cay_"
decl_stmt|;
specifier|private
name|ClassVisitor
name|classVisitor
decl_stmt|;
specifier|private
name|Type
name|currentClass
decl_stmt|;
specifier|public
name|EnhancementHelper
parameter_list|(
name|ClassVisitor
name|classVisitor
parameter_list|)
block|{
name|this
operator|.
name|classVisitor
operator|=
name|classVisitor
expr_stmt|;
block|}
specifier|public
name|Type
name|getCurrentClass
parameter_list|()
block|{
return|return
name|currentClass
return|;
block|}
specifier|public
name|String
name|getPropertyField
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|fieldPrefix
operator|+
name|propertyName
return|;
block|}
comment|/**      * Resets helper to process a given class. Must be called repeatedly before each class      * is processed.      */
specifier|public
name|void
name|reset
parameter_list|(
name|String
name|className
parameter_list|)
block|{
comment|// assuming no primitives or arrays
name|this
operator|.
name|currentClass
operator|=
name|Type
operator|.
name|getType
argument_list|(
literal|"L"
operator|+
name|className
operator|+
literal|";"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends an interface to a String array of interfaces, returning the resulting      * expanded array.      */
specifier|public
name|String
index|[]
name|addInterface
parameter_list|(
name|String
index|[]
name|interfaces
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|newInterface
parameter_list|)
block|{
name|String
name|name
init|=
name|Type
operator|.
name|getInternalName
argument_list|(
name|newInterface
argument_list|)
decl_stmt|;
if|if
condition|(
name|interfaces
operator|==
literal|null
operator|||
name|interfaces
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|String
index|[]
block|{
name|name
block|}
return|;
block|}
name|String
index|[]
name|expandedInterfaces
init|=
operator|new
name|String
index|[
name|interfaces
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|expandedInterfaces
index|[
literal|0
index|]
operator|=
name|name
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|interfaces
argument_list|,
literal|0
argument_list|,
name|expandedInterfaces
argument_list|,
literal|1
argument_list|,
name|interfaces
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|expandedInterfaces
return|;
block|}
comment|/**      * Creates a new protected field in the current class. Field name will be      * automatically prefixed by "$cay_".      */
specifier|public
name|void
name|createField
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|fieldType
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|createField
argument_list|(
name|fieldType
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new protected field in the current class. Field name will be      * automatically prefixed by "$cay_".      */
specifier|public
name|void
name|createField
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|fieldType
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|isTransient
parameter_list|)
block|{
name|Type
name|asmType
init|=
name|Type
operator|.
name|getType
argument_list|(
name|fieldType
argument_list|)
decl_stmt|;
name|int
name|access
init|=
name|Opcodes
operator|.
name|ACC_PROTECTED
decl_stmt|;
if|if
condition|(
name|isTransient
condition|)
block|{
name|access
operator|+=
name|Opcodes
operator|.
name|ACC_TRANSIENT
expr_stmt|;
block|}
name|createField
argument_list|(
name|name
argument_list|,
name|asmType
argument_list|,
name|access
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createProperty
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|propertyType
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|createProperty
argument_list|(
name|propertyType
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|createProperty
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|propertyType
parameter_list|,
name|String
name|name
parameter_list|,
name|boolean
name|isTransient
parameter_list|)
block|{
name|Type
name|asmType
init|=
name|Type
operator|.
name|getType
argument_list|(
name|propertyType
argument_list|)
decl_stmt|;
name|int
name|access
init|=
name|Opcodes
operator|.
name|ACC_PROTECTED
decl_stmt|;
if|if
condition|(
name|isTransient
condition|)
block|{
name|access
operator|+=
name|Opcodes
operator|.
name|ACC_TRANSIENT
expr_stmt|;
block|}
name|createField
argument_list|(
name|name
argument_list|,
name|asmType
argument_list|,
name|access
argument_list|)
expr_stmt|;
name|createGetter
argument_list|(
name|name
argument_list|,
name|asmType
argument_list|)
expr_stmt|;
name|createSetter
argument_list|(
name|name
argument_list|,
name|asmType
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createSetter
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Type
name|asmType
parameter_list|)
block|{
name|String
name|methodName
init|=
literal|"set"
operator|+
name|Character
operator|.
name|toUpperCase
argument_list|(
name|propertyName
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|propertyName
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|methodName
operator|+=
name|propertyName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|MethodVisitor
name|mv
init|=
name|classVisitor
operator|.
name|visitMethod
argument_list|(
name|Opcodes
operator|.
name|ACC_PUBLIC
argument_list|,
name|methodName
argument_list|,
literal|"("
operator|+
name|asmType
operator|.
name|getDescriptor
argument_list|()
operator|+
literal|")V"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|mv
operator|.
name|visitCode
argument_list|()
expr_stmt|;
name|Label
name|l0
init|=
operator|new
name|Label
argument_list|()
decl_stmt|;
name|mv
operator|.
name|visitLabel
argument_list|(
name|l0
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
comment|// TODO: andrus, 10/9/2006 other opcodes
if|if
condition|(
literal|"I"
operator|.
name|equals
argument_list|(
name|asmType
operator|.
name|getDescriptor
argument_list|()
argument_list|)
condition|)
block|{
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
block|}
else|else
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
name|visitFieldInsn
argument_list|(
name|Opcodes
operator|.
name|PUTFIELD
argument_list|,
name|currentClass
operator|.
name|getInternalName
argument_list|()
argument_list|,
name|getPropertyField
argument_list|(
name|propertyName
argument_list|)
argument_list|,
name|asmType
operator|.
name|getDescriptor
argument_list|()
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitInsn
argument_list|(
name|Opcodes
operator|.
name|RETURN
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
name|visitLabel
argument_list|(
name|l1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitLocalVariable
argument_list|(
literal|"this"
argument_list|,
name|currentClass
operator|.
name|getDescriptor
argument_list|()
argument_list|,
literal|null
argument_list|,
name|l0
argument_list|,
name|l1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitLocalVariable
argument_list|(
name|propertyName
argument_list|,
name|asmType
operator|.
name|getDescriptor
argument_list|()
argument_list|,
literal|null
argument_list|,
name|l0
argument_list|,
name|l1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMaxs
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|createGetter
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Type
name|asmType
parameter_list|)
block|{
name|String
name|prefix
init|=
literal|"boolean"
operator|.
name|equals
argument_list|(
name|asmType
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|?
literal|"is"
else|:
literal|"get"
decl_stmt|;
name|String
name|methodName
init|=
name|prefix
operator|+
name|Character
operator|.
name|toUpperCase
argument_list|(
name|propertyName
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|propertyName
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|methodName
operator|+=
name|propertyName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|MethodVisitor
name|mv
init|=
name|classVisitor
operator|.
name|visitMethod
argument_list|(
name|Opcodes
operator|.
name|ACC_PUBLIC
argument_list|,
name|methodName
argument_list|,
literal|"()"
operator|+
name|asmType
operator|.
name|getDescriptor
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|mv
operator|.
name|visitCode
argument_list|()
expr_stmt|;
name|Label
name|l0
init|=
operator|new
name|Label
argument_list|()
decl_stmt|;
name|mv
operator|.
name|visitLabel
argument_list|(
name|l0
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
name|currentClass
operator|.
name|getInternalName
argument_list|()
argument_list|,
name|getPropertyField
argument_list|(
name|propertyName
argument_list|)
argument_list|,
name|asmType
operator|.
name|getDescriptor
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: andrus, 10/9/2006 other return opcodes
if|if
condition|(
literal|"I"
operator|.
name|equals
argument_list|(
name|asmType
operator|.
name|getDescriptor
argument_list|()
argument_list|)
condition|)
block|{
name|mv
operator|.
name|visitInsn
argument_list|(
name|Opcodes
operator|.
name|IRETURN
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mv
operator|.
name|visitInsn
argument_list|(
name|Opcodes
operator|.
name|ARETURN
argument_list|)
expr_stmt|;
block|}
name|Label
name|l1
init|=
operator|new
name|Label
argument_list|()
decl_stmt|;
name|mv
operator|.
name|visitLabel
argument_list|(
name|l1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitLocalVariable
argument_list|(
literal|"this"
argument_list|,
name|currentClass
operator|.
name|getDescriptor
argument_list|()
argument_list|,
literal|null
argument_list|,
name|l0
argument_list|,
name|l1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitMaxs
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mv
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|createField
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Type
name|asmType
parameter_list|,
name|int
name|access
parameter_list|)
block|{
name|FieldVisitor
name|fv
init|=
name|classVisitor
operator|.
name|visitField
argument_list|(
name|access
argument_list|,
name|getPropertyField
argument_list|(
name|propertyName
argument_list|)
argument_list|,
name|asmType
operator|.
name|getDescriptor
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|fv
operator|.
name|visitEnd
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

