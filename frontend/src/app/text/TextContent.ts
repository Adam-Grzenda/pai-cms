export class TextContent {
  id: number;
  title: string;
  subtitle: string;
  content: string;
  tags: string[]
}

export class TextContentPage {
  content: TextContent[];
  totalPages: number;
}
