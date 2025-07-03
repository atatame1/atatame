import { useRouter } from "vue-router";

export default function(url: string)  {
    const router = useRouter()
    const turnBack=() => {
        router.push(url)
    }
    return { turnBack }
}
