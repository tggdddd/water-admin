import {BasicColumn, FormSchema} from '/@/components/Table';
import {JVxeColumn, JVxeTypes} from '/@/components/jeecg/JVxeTable/types'
//列表数据
export const columns: BasicColumn[] = [
    {
        title: '分类',
        align: "center",
        dataIndex: 'typeId_dictText'
    },
    {
        title: '名称',
        align: "center",
        dataIndex: 'name'
    },
    {
        title: '简介',
        align: "center",
        dataIndex: 'biref'
    },
    {
        title: '详情',
        align: "center",
        dataIndex: 'detail',
        slots: {customRender: 'htmlSlot'},
    },
    {
        title: '图片',
        align: "center",
        dataIndex: 'imageString'
    },
    {
        title: '详情图片',
        align: "center",
        dataIndex: 'imagesString'
    },
    {
        title: '单位',
        align: "center",
        dataIndex: 'unit'
    },
    {
        title: '是否启用',
        align: "center",
        dataIndex: 'status_dictText'
    },
    {
        title: '销量',
        align: "center",
        dataIndex: 'sale'
    },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
    {
        label: "创建日期",
        field: "createTime",
        component: 'RangePicker',
        componentProps: {
            valueType: 'Date',
            showTime: true
        },
        colProps: {span: 6},
    },
    {
        label: "名称",
        field: "name",
        component: 'Input',
        colProps: {span: 6},
    },
    {
        label: "是否启用",
        field: "status",
        component: 'JDictSelectTag',
        componentProps: {
            dictCode: "yes_or_no"
        },
        colProps: {span: 6},
    },
];
//表单数据
export const formSchema: FormSchema[] = [
    {
        label: '分类',
        field: 'typeId',
        component: 'JDictSelectTag',
        componentProps: {
            dictCode: "water_class,name,id"
        },
        dynamicRules: ({model, schema}) => {
            return [
                {required: true, message: '请输入分类!'},
            ];
        },
    },
    {
        label: '名称',
        field: 'name',
        component: 'Input',
        dynamicRules: ({model, schema}) => {
            return [
                {required: true, message: '请输入名称!'},
            ];
        },
    },
    {
        label: '简介',
        field: 'biref',
        component: 'InputTextArea',
        dynamicRules: ({model, schema}) => {
            return [
                {required: true, message: '请输入简介!'},
            ];
        },
    },
    {
        label: '详情',
        field: 'detail',
        component: 'JEditor',
    },
    {
        label: '图片',
        field: 'imageString',
        component: 'JImageUpload',
        componentProps: {},
        dynamicRules: ({model, schema}) => {
            return [
                {required: true, message: '请输入图片!'},
            ];
        },
    },
    {
        label: '详情图片',
        field: 'imagesString',
        component: 'JImageUpload',
        componentProps: {},
    },
    {
        label: '单位',
        field: 'unit',
        component: 'Input',
        dynamicRules: ({model, schema}) => {
            return [
                {required: true, message: '请输入单位!'},
            ];
        },
    },
    {
        label: '是否启用',
        field: 'status',
        component: 'JDictSelectTag',
        componentProps: {
            dictCode: "yes_or_no"
        },
    },
    // TODO 主键隐藏字段，目前写死为ID
    {
        label: '',
        field: 'id',
        component: 'Input',
        show: false
    },
];
//子表单数据
//子表表格配置
export const waterShopModelColumns: JVxeColumn[] = [
    {
        title: '型号/规格',
        key: 'model',
        type: JVxeTypes.input,
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
        ],
    },
]
export const waterShopItemColumns: JVxeColumn[] = [
    {
        title: '商品编码',
        key: 'code',
        type: JVxeTypes.input,
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
        ],
    },
    {
        title: '商品名称',
        key: 'name',
        type: JVxeTypes.input,
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
        ],
    },
    {
        title: '型号/规格',
        key: 'model',
        type: JVxeTypes.select,
        options: [],
        dictCode: "water_shop_model,model,id",
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
        ],
    },
    {
        title: '成本',
        key: 'cost',
        type: JVxeTypes.input,
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
            {pattern: "money", message: "${title}格式不正确"}
        ],
    },
    {
        title: '零售',
        key: 'retail',
        type: JVxeTypes.input,
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
            {pattern: "money", message: "${title}格式不正确"}
        ],
    },
    {
        title: '重量(kg)',
        key: 'weight',
        type: JVxeTypes.input,
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
            {pattern: "n", message: "${title}格式不正确"}
        ],
    },
    {
        title: '库存',
        key: 'reserve',
        type: JVxeTypes.input,
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
            {pattern: "z", message: "${title}格式不正确"}
        ],
    },
    {
        title: '是否启用',
        key: 'status',
        type: JVxeTypes.select,
        options: [],
        dictCode: "yes_or_no",
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
        validateRules: [
            {required: true, message: '${title}不能为空'},
        ],
    },
    {
        title: '图片',
        key: 'imageString',
        type: JVxeTypes.image,
        token: true,
        responseName: "message",
        width: "200px",
        placeholder: '请输入${title}',
        defaultValue: '',
    },
]


/**
 * 流程表单调用这个方法获取formSchema
 * @param param
 */
export function getBpmFormSchema(_formData): FormSchema[] {
// 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
    return formSchema;
}