export interface Project {
    id: number;
    name: string;
    description: string | null;
    repoUrl: string | null;
    createdAt: string;
}

export interface CreateProjectRequest {
    name: string;
    description: string | null;
    repoUrl: string | null;
}
