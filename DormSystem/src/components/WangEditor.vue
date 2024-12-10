<script setup>
import '@wangeditor/editor/dist/css/style.css' // 引入 css

import {onBeforeUnmount, ref, shallowRef, onMounted, watch} from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import {useTokenStore} from "@/store/token.js";

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('<p></p>')

// 接收父组件传来的值
// props 是组件之间传递数据的机制。它们是组件的自定义属性，用于将数据从父组件传递到子组件
// defineProps() 是 Vue 3 中用于定义组件 props 的一个函数，它简化了 props 的声明过程
// 可以理解为这是定义的一个属性
const props = defineProps({
    initValue: {
        type: String,
        default: ''
    }
})

// defineEmits用于定义组件的事件
const emit = defineEmits(['getEditorContent'])

const token = useTokenStore()

// 监听父组件传的值的变化，用于回显
watch(() => props.initValue, value => {
    valueHtml.value = value ?? ''
})

// 监听富文本编辑器内容，用于获取数据
watch(valueHtml, (value) => {
    emit('getEditorContent', value)
})

// 模拟 ajax 异步获取内容
/*onMounted(() => {
    setTimeout(() => {
        valueHtml.value = '<p>模拟 Ajax 异步设置内容</p>'
    }, 1500)
})*/

const toolbarConfig = {}
const editorConfig = {
    placeholder: '请输入内容...',
    MENU_CONF: {
        uploadImage: {
            server: '/api/upload',
            fieldName: 'file',
            headers: {
                Authorization: token.token
            },
            // 自定义插入图片
            customInsert(res, insertFn) {
                // res 即服务端的返回结果
                console.log(res);
                // 从 res 中找到 url alt href ，然后插入图片
                insertFn(res.imgName)
            },
        }
    }
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
    const editor = editorRef.value
    if (editor == null) return
    editor.destroy()
})

const handleCreated = (editor) => {
    editorRef.value = editor // 记录 editor 实例，重要！
}
</script>

<template>
    <div style="border: 1px solid #ccc">
        <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :defaultConfig="toolbarConfig"
                :mode="mode"
        />
        <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="valueHtml"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="handleCreated"
        />
    </div>
</template>

<style scoped>

</style>